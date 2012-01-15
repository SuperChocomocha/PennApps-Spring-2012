# creates a connection to the app
# gets json data from the connection
# stores data into database
# queries database and runs comparison algorithm
# returns result of comparison

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

# create socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((get_ip_address('wlan0'), __PORT))

s.listen(__MAX_CONNECT)

# start accepting connections and grabbing data
while 1:
    # (client, address) = s.accept()
    # data = json.loads(client.recv(__RECV_BUF))

    query = sqldb.cursor()
    query.execute("""
                  SELECT artist FROM test_artist
                  WHERE user_id != %s
                  """, (5)) # generate id
    result = query.fetchall()

    for item in result:
        print item
        print compare("Metallica", item[0]) # first argument is user id artist
    
    query.execute("""
                  SELECT user_id FROM test_artist
                  WHERE user_id = %s
                  """, (5)) # make general
    result = query.fetchall()
    
    if result:
        writer.execute("""
                       UPDATE test_artist
                       SET song = %s,
                           artist = %s,
                           album = %s
                       WHERE
                           user_id = %s
                       """, ("Oroborus", "Gojira", "The Art of Dying", 1)) # generalize input
    else:
        writer.execute("""
                       INSERT INTO test_artist (user_id, song, artist, album)
                       VALUES
                       (%s, %s, %s, %s)
                       """, (5 , "Truce", "Death Angel", "Relentless Retribution")) # generalize input

    # client.send("bitch")
    # query.close()
    # client.close()
    break

# close socket and db
writer.close()
s.close()

