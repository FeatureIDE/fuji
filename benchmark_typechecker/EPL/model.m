ExpressionProblem : [Derivatives] Operations Structures base :: _ExpressionProblem ;

Derivatives : [Eval_Neg] [Eval_Plus] [Eval_Numbers] [Neg_ToString] [Plus_ToString] [Numbers_ToString] :: _Derivatives ;

Operations : [Eval] [ToString] :: _Operations ;

Structures : [Neg] [Plus] Numbers :: _Structures ;

%%

Numbers and ToString implies Numbers_ToString ;
Plus and ToString implies Plus_ToString ;
Neg and ToString implies Neg_ToString ;
Eval and Numbers implies Eval_Numbers ;
Eval and Plus implies Eval_Plus ;
Eval and Neg implies Eval_Neg ;

