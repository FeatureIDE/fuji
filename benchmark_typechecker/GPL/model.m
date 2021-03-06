// grammar of feature mod
// domain constrain
// implementation constrain
// formatti
GPL : TestProg Alg+ Src HiddenWgt Wgt HiddenGtp Gtp Implementation Base :: MainGpl ;

Alg : Number
	| Connected
	| StronglyConnected Transpose :: StrongC
	| Cycle
	| MSTPrim
	| MSTKruskal ;

Src : BFS
	| DFS ;

HiddenWgt : [WeightedWithEdges] [WeightedWithNeighbors] [WeightedOnlyVertices] :: WeightOptions ;

Wgt : Weighted
	| UnWeighted ;

HiddenGtp : DirectedWithEdges
	| DirectedWithNeighbors
	| DirectedOnlyVertices
	| UndirectedWithEdges
	| UndirectedWithNeighbors
	| UndirectedOnlyVertices ;

Gtp : Directed
	| UnDirected ;

Implementation : OnlyVertices
	| WithNeighbors
	| WithEdges ;

%%

Number implies Gtp and Src ;
Connected implies UnDirected and Src ;
StrongC implies Directed and DFS ;
Cycle implies Gtp and DFS ;
MSTKruskal or MSTPrim implies UnDirected and Weighted ;
MSTKruskal or MSTPrim implies not (MSTKruskal and MSTPrim) ;
MSTKruskal implies WithEdges ;
OnlyVertices and Weighted implies WeightedOnlyVertices ;
WithNeighbors and Weighted implies WeightedWithNeighbors ;
WithEdges and Weighted implies WeightedWithEdges ;
OnlyVertices and Directed implies DirectedOnlyVertices ;
WithNeighbors and Directed implies DirectedWithNeighbors ;
WithEdges and Directed implies DirectedWithEdges ;
OnlyVertices and UnDirected implies UndirectedOnlyVertices ;
WithNeighbors and UnDirected implies UndirectedWithNeighbors ;
WithEdges and UnDirected implies UndirectedWithEdges ;

##

HiddenGtp { hidden } 
HiddenWgt { hidden } 
