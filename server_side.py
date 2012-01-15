# connect to MySQL database
# creates a connection to the app
# gets data from the connection
# stores data into database
# queries database and runs comparison algorithm
# returns result of comparison

import ast
import socket
import fcntl
import struct
import MySQLdb
import json
from algo import compare

# socket configuration
__PORT = 80
__RECV_BUF = 4096
__MAX_CONNECT = 5

# get IP address, workaround to grab correct IP
# because gethostname() and gethostbyname() was returning localhost
# 0x8915 is SIOCGIFADDR
def get_ip_address(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return socket.inet_ntoa(fcntl.ioctl(s.fileno(), 0x8915,
                                struct.pack('256s', ifname[:15]))[20:24])

# connect to database
sqldb = MySQLdb.connect(host = "", user = "", passwd = "", db = "")
writer = sqldb.cursor()
query = sqldb.cursor()

# create socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((get_ip_address('wlan0'), __PORT))

s.listen(__MAX_CONNECT)

# accept connections, grab data, analyze data
while 1:
    # connect to a client
    (client, address) = s.accept()
    
    # grabs data, splits on newline, strips "json: " and whitespace, and converts into a dict
    data = ast.literal_eval(str.strip(str.split(client.recv(__RECV_BUF), '\n')[1][6:]))

    # check if it's song info or location info
    if 'latitude' in data:
        query.execute("""
                      SELECT fid FROM location_info
                      WHERE fid = %s
                      """, (data['FID']))
        result = query.fetchall()

        if result: # user exists
            writer.execute("""
                           UPDATE location_info
                           SET latitude = %s,
                               longitude = %s
                           WHERE
                               fid = %s
                          """, (data['lat'], data['long'], data['FID']))
        else: # user doesn't exist
            writer.execute("""
                           INSERT INTO location_info (fid, latitude, longitude)
                           VALUES
                           (%s, %s, %s)
                          """, (data['FID'] , data['lat'], data['long']))
    else:
        query.execute("""
                      SELECT fid FROM song_info
                      WHERE fid = %s
                      """, (data['FID']))
        result = query.fetchall()

        if result:
            writer.execute("""
                           UPDATE song_info
                           SET album = %s,
                               title = %s,
                               name = %s,
                               artist = %s
                           WHERE
                               fid = %s
                          """, (data['Album'], data['Title'], data['Name'], data['Artist'], data['FID']))
        else:
            writer.execute("""
                           INSERT INTO song_info (fid, album, title, name, artist)
                           VALUES
                           (%s, %s, %s, %s, %s)
                          """, (data['FID'] , data['Album'], data['Title'], data['Name'], data['Artist']))
    
    # grab all users currently in database that is not the connected user
    query.execute("""
                  SELECT artist, latitude, longitude, album, title, name FROM joined_info
                  WHERE fid != %s
                  """, (data['FID']))
    result = query.fetchall()
    
    # grab current user's data
    query.execute("""
                  SELECT artist, latitude, longitude FROM joined_info
                  WHERE fid = %s
                  """, (data['FID']))
    current_user= query.fetchall() # check if always one output

    # run compare on all pairs of (current user artist, other user artist, current location, other location)
    r = []
    if result and current_user:
        for item in result:
            b = compare(current_user[0], item[0], (current_user[1], current_user[2]), (item[1], item[2]))
            if b:
                r.append({"Artist": item[0], "Latitude": item[1], "Longitude": item[2], "Album": item[3], "Title": item[4], "Name": item[5]})
    
    # send results of compare back to current user and close connection
    client.send(json.dumps({"k": r}))
    client.close()

# close socket and db
query.close()
writer.close()
s.close()

