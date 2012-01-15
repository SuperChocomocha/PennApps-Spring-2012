import time
import pylast

API_KEY = ""
API_SECRET = ""

ARTIST_A = "Symphony X"
ARTIST_B = "Death Angel"

W_S = 0.5 # weight on similar artists
W_T = 0.5 # weight on similar tags
THRESHOLD = 0.2 # min score to be similar

SIMILAR_LIMIT = 20 # number of similar artists to examine
TAG_LIMIT = 5

## Handle for Last.FM API calls
network = pylast.LastFMNetwork(api_key = API_KEY, api_secret = API_SECRET)

def compute(user, loc):
    pass

## Comparison algorithm that returns whether artist1 and artist2 are similar
def compare(artist1, artist2):

    ## Grab the top search result for the artists from Last.fm
    # search takes ~2-3 sec
    #a1 = network.search_for_artist(artist1).get_next_page()[0]
    #a2 = network.search_for_artist(artist2).get_next_page()[0]
    a1 = network.get_artist(artist1)
    a2 = network.get_artist(artist2)

    ## Grab similar artists
    similar = a1.get_similar(limit=SIMILAR_LIMIT)
    similar_artists_1 = map(lambda x: x[0], similar) # grab Artists
    #similar = a2.get_similar(limit=SIMILAR_LIMIT)
    #similar_artists_2 = map(lambda x: x[0], similar) # grab Artists

    ## Grab top tags
    tag_items = a1.get_top_tags(limit=TAG_LIMIT)
    tags_1 = set(map(lambda x: x[0].get_name(), tag_items))
    tag_items = a2.get_top_tags(limit=TAG_LIMIT)
    tags_2 = set(map(lambda x: x[0].get_name(), tag_items))

    #print tags_1, tags_2, tags_1 & tags_2

    #print map(lambda x: x.get_name(), similar_artists)

    s = a2 in similar_artists_1 #or a1 in similar_artists_2
    t = len(tags_1 & tags_2) / (1.0 * TAG_LIMIT)

    score =  W_S * s + W_T * t

    #print score
    return score >= THRESHOLD

#t1 = time.time()
#print ARTIST_A, ARTIST_B, compare(ARTIST_A, ARTIST_B)
#t2 = time.time()
#print "took %0.3f s" % (t2-t1)

if __name__ == "__main__":
    import doctest
    doctest.testfile("tests.txt")
