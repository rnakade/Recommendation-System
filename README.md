# Recommendation-System
A collaborative filtering based recommender system built using Apache Mahout.

This is a collaborative filtering based recommender system built using Apache Mahout. A recommender system that uses collaborative filtering provides recommendations to users based on the collective preferences of many users. Apache Mahout is an open source software library for implementing a collaborative filtering recommender system. 

There are two basic approaches to a collaborative filtering recommender system: user-based recommendations and item-based recommendations. 

Item-Based Recommender:
Given a particular item i, an item-based recommender will recommend other items similar to i based on a metric that calculates the similarity between two items. Here is the pseudo-code for generating recommendations:

for every item i for which user u does not yet have a preference value:
    for every item j for which u has a preference value:
        compute similarity s between i and j
        add u’s computed preference for j, weighted by s, to a running average preference for i
return the top items, ranked by weighted average

User Based Recommender:
Given a particular customer u, a user-based recommender will recommend items for i based on i’s similarity to other users and those other users preferences. Here is the pseudo-code:

for every item i for which user u does not yet have a preference value:
    for every other user v that has a preference for i:
        compute a similarity s between u and v
        incorporate v’s preference for i, weighted by s, into a running average preference for i
return the top items, ranked by weighted average

The above pseudo-code is pretty computationally heavy because you need to loop through every user for every item for which user u doesn’t have a preference value. To decrease the computational load we generate a user neighborhood for each user and only consider a user’s neighborhood when computing the similarity value:

for every other user w:
    compute a similarity s between user u and w
    retain the top users, ranked by similarity s, as a neighbor n

for every item i that some user in n has preference for, but for which u does not yet have a preference:
    for every other user v in n that has a preference for i:
        compute a similarity s between u and v
        incorporate v’s preference for i, weighted by s, into a running average for item i
return the top items, ranked by weighted average
