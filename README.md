# QuadTree Indexing.

Java implementation for QuadTree Indexing.
QuadTree Indexing Assumption : Index bounding is the whole world Create QuadTree with the
maxCapacity Integer which is the max number of Items(DataEntry for this Implementation).


Classes
----------
GeoLocation: represent point on the map using latitude and longitude.

DataEntry: indexed object, for simplicity DataEntry just has id and location.

QuadTreeNode: Node(Quad) in the QuadTree, Each Node has Bounding Box, 4 children(if internal node)
or zero children (if leaf node) data stored in the leaf node (like B+ tree).

QuadTree: QuadTree Indexer with Index and search functions.

---------------------------------------------------------------------------------------------------

Indexing Function:
For good performance, use bulk loading instead of index items individually, Index bulk of data.
WHY?
TO run sundividing after some insertion is better than run after each item insertion.

Searching Function,
return list of data interseted with the given bounding box
