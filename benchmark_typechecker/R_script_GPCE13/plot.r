draft = F
#if (draft) dev.off()


options(width=180)

ca=commandArgs(trailingOnly=TRUE) #only args after --args
if (draft) ca=c("EPL", "GPL", "GUIDSL", "Prevayler")

#interpret all elements of ca as names of case studies
caseStudies = ca[!is.na(ca)]

getRepetitionData <- function(repetitionNumber) {
	csInternalData = vector("list",length(caseStudies))
	csExternalData = vector("list",length(caseStudies))
	csExternalData_features = vector("list",length(caseStudies))
	csInternalData_features = vector("list",length(caseStudies))
	csInternalData_fam = vector("list",length(caseStudies))
	for (i in 1:length(caseStudies)) {
	  cat(i, ") reading case study data (repetition ", repetitionNumber, ")  \"",caseStudies[i],"\"\n", sep="")
	  int = read.csv(file=paste("../results_repetition",repetitionNumber,"/",caseStudies[i],"/inttimetypechecker.csv",sep=""),head=TRUE, sep="\t", na.strings=c("","NA"))
	  ext = read.csv(file=paste("../results_repetition",repetitionNumber,"/",caseStudies[i],"/exttimetypechecker.csv",sep=""),head=TRUE, sep="\t", na.strings=c("","NA"))
	  
	  csInternalData_features[[i]] <- read.csv(file=paste("../results_repetition",repetitionNumber,"/",caseStudies[i],"/inttimetypechecker_featurebased.csv",sep=""),head=TRUE, sep="\t", na.strings=c("","NA"))
	  csExternalData_features[[i]] <- read.csv(file=paste("../results_repetition",repetitionNumber,"/",caseStudies[i],"/exttimetypechecker_featurebased.csv",sep=""),head=TRUE, sep="\t", na.strings=c("","NA"))

	  csInternalData_features[[i]][is.na(csInternalData_features[[i]])] <- c(0)
	  int[is.na(int)] <- c(0)
		
	  csInternalData_fam[[i]] = int[int$variant == "family", ]
	  
	  int = int[int$variant != "family", ]
	  ext = ext[ext$variant != "family", ]
	  
	  if (nrow(int) != nrow(ext)) {
		stop("inconsistent input tables")
	  }
	  #if (! all(int['variant'] == ext['variant'])) {
	  #	stop("inconsistent variants in input tables")
	  #}
	  csInternalData[[i]]<-int
	  csExternalData[[i]]<-ext
	}
	list(csInternalData=csInternalData, csExternalData=csExternalData, 
				csExternalData_features=csExternalData_features, csInternalData_features=csInternalData_features,
				csInternalData_fam=csInternalData_fam)
}
rep_data=list(getRepetitionData(1), getRepetitionData(2), getRepetitionData(3), 
getRepetitionData(4), getRepetitionData(5), getRepetitionData(6), 
getRepetitionData(7), getRepetitionData(8), getRepetitionData(9))

#get data without optimization
csInternalData_fam_noOpt = vector("list",length(caseStudies))
for (i in 1:length(caseStudies)) {
	cat(i, ") reading case study data (noOptimization) \"",caseStudies[i],"\"\n", sep="")
	int_noOpt = read.csv(file=paste("../resultBackup_family-based_no_optimization_rev1102/",caseStudies[i],"/inttimetypechecker.csv",sep=""),
			colClasses=c("character",rep("numeric",3)),head=TRUE, sep="\t", na.strings=c("","NA"))
	  ext_noOpt = read.csv(file=paste("../resultBackup_family-based_no_optimization_rev1102/",caseStudies[i],"/exttimetypechecker.csv",sep=""),head=TRUE, sep="\t", na.strings=c("","NA"))
	  int_noOpt[is.na(int_noOpt)] <- c(0)
	  csInternalData_fam_noOpt[[i]] = int_noOpt[int_noOpt$variant=="family", ]
}

bytecodeCompTimes = read.csv(file=paste("bytecode_typecheck_complete.csv",sep=""),head=TRUE, sep="\t", na.strings=c("","NA"))
print("bytecodecomp:")
print(bytecodeCompTimes[bytecodeCompTimes$name=="GPL",])

getFeatureTimeInt <- function(caseStudyID) {
  astComp = c()
  typecheck = c()
	for (i in 1:length(rep_data)) {
		prodlines = rep_data[[i]]$csInternalData_features[[caseStudyID]]
		astComp[[i]] <- sum((prodlines$ASTcomp))
		typecheck[[i]] <- sum((prodlines$typecheck))
	}
	cat ("relativer Messfehler:sd/mean\n",
		"cs ", caseStudies[[caseStudyID]], " featurebased\n",
		"astComp ", 100*sd(astComp)/mean(astComp), "%\ttypecheck ", 100*sd(typecheck)/mean(typecheck),"%\n")
	c(mean(astComp), mean(typecheck))
}
getFeatureTimeInt_wBytecodeComp <- function(caseStudyID) {
	bytecodeCompTime=bytecodeCompTimes[caseStudyID, "bytecode_typecheck"]
	astComp = c()
	typecheck = c()
  for (i in 1:length(rep_data)) {
		prodlines = rep_data[[i]]$csInternalData_features[[caseStudyID]]
		astComp[[i]] <- sum((prodlines$ASTcomp))
		typecheck[[i]] <- sum((prodlines$typecheck))
	}
	cat ("relativer Messfehler:sd/mean\n",
		"cs ", caseStudies[[caseStudyID]], " feature_wBytecodeComp\n",
		"astComp ", 100*sd(astComp)/mean(astComp), "%\ttypecheck ", 100*sd(typecheck)/mean(typecheck),"%\n")
		# bytecode comp is a single value, statically read out of a file, standard deviation makes no sense
	c(mean(astComp), mean(typecheck),sum((bytecodeCompTime)))
}
getProdTimeInt <- function(caseStudyID) {
	astComp = c()
	typecheck = c()
	for (i in 1:length(rep_data)) {
		prodlines = rep_data[[i]]$csInternalData[[caseStudyID]]
		astComp[[i]] <- sum((prodlines$ASTcomp))
		typecheck[[i]] <- sum((prodlines$typecheck))
	}
	cat ("relativer Messfehler:sd/mean\n",
		"cs ", caseStudies[[caseStudyID]], " productbased\n",
		"astComp ", 100*sd(astComp)/mean(astComp), "%\ttypecheck ", 100*sd(typecheck)/mean(typecheck),"%\n")
  c(mean(astComp), mean(typecheck))
}
getFamTimeInt <- function(caseStudyID) {
  astComp = c()
  typecheck = c()
	for (i in 1:length(rep_data)) {
		prodlines = rep_data[[i]]$csInternalData_fam[[caseStudyID]]
		astComp[[i]] <- sum((prodlines$ASTcomp))
		typecheck[[i]] <- sum((prodlines$typecheck))
	}
	cat ("relativer Messfehler:sd/mean\n",
		"cs ", caseStudies[[caseStudyID]], " familybased\n",
		"astComp ", 100*sd(astComp)/mean(astComp), "%\ttypecheck ", 100*sd(typecheck)/mean(typecheck),"%\n")
  c(mean(astComp), mean(typecheck))
}

getFamTimeInt_noOpt <- function(caseStudyID) {
  prodlines = csInternalData_fam_noOpt[[caseStudyID]]
  c(sum((prodlines$ASTcomp)),sum((prodlines$typecheck)))
}

plotData <- matrix(nrow=2, ncol=length(caseStudies))
for (i in 1:length(caseStudies)) {
  plotData[,i] = getProdTimeInt(i)/1000 # product time
}
#print (plotData)

plotDataFeat <- matrix(nrow=2, ncol=length(caseStudies))
for (i in 1:length(caseStudies)) {
  plotDataFeat[,i] = getFeatureTimeInt(i)/1000 # feature time
}
#print (plotDataFeat)

plotDataFam <- matrix(nrow=2, ncol=length(caseStudies))
for (i in 1:length(caseStudies)) {
  plotDataFam[,i] = getFamTimeInt(i)/1000 # fam time
}
#print (plotDataFam)

plotDataFeat_wBytecodeComp <- matrix(nrow=3, ncol=length(caseStudies))
for (i in 1:length(caseStudies)) {
  plotDataFeat_wBytecodeComp[,i] = getFeatureTimeInt_wBytecodeComp(i)/1000 # feature time
}
print (plotDataFeat_wBytecodeComp)

plotDataFam_noOpt <- matrix(nrow=2, ncol=length(caseStudies))
for (i in 1:length(caseStudies)) {
  plotDataFam_noOpt[,i] = getFamTimeInt_noOpt(i)/1000 # fam time no opt
}
#print (plotDataFam_noOpt)

getMaxY <- function(plotData, plotDataFeat, plotDataFam, plotDataFam_noOpt) {
  maximum = 0
  for (i in 1:ncol(plotData)) {
    maximum = max( sum(plotData[,i]), sum(plotDataFeat[,i]), sum(plotDataFam[,i]), sum(plotDataFeat_wBytecodeComp[,i]), sum(plotDataFam_noOpt[,i]), maximum)
  }
  maximum
}

if (draft) { # use colors
	color <- c("lightblue", "lightskyblue", # prod
				"tomato2", "firebrick3", # feat
				"olivedrab2", "chartreuse3" # fam
	)
	textcolor="firebrick3"
} else { # use black/white
	color <- c(rgb(0.75, 0.75, 0.75), rgb(1, 1, 1), rgb(.35, .35, .35)) # setup, typecheck, bytecodeComp
	textcolor=rgb(0, 0, 0)
}
if (!draft) pdf(file=paste("plot_int.","pdf",sep=""), width=11, height=10, onefile=TRUE, paper="special") 

layoutMat=matrix(c(
	# 37 is the legend1
	# 38 is the legend2
	# 39 is free
	3,3,   6,6,   9,9,   39, #titles
	1,2,   4,5,   7,8,   37, #plots
	12,12, 15,15, 18,18, 39, #titles
	10,11, 13,14, 16,17, 38, #plots
	21,21, 24,24, 27,27, 39, #titles
	19,20, 22,23, 25,26, 39, #plots
	30,30, 33,33, 36,36, 39, #titles
	28,29, 31,32, 34,35, 39  #plots
), 8, 7, byrow = TRUE)
layoutHeights=c(0.2,1, 0.2,1, 0.2,1, 0.2,1)
layoutWidths=c(1,0.8, 1,0.8, 1,0.8, 1.3)
layout(mat=layoutMat, heights=layoutHeights, widths=layoutWidths)

for (i in 1:length(caseStudies)) {  
  paintLog=FALSE
  #log="y"
  log=""
  #for(logcs in c("EPL", "GPL", "GUIDSL", "Notepad", "TankWar", "Violet"))  {
  #for(logcs in c())  {
	#if (caseStudies[i]==logcs) {
	#	paintLog=TRUE
	#	log="y"
	#}
  #}
  #if (i != 1) par(new=TRUE)
  maxY = max (	sum (t(t(plotData[,i]))),
				sum(t(t(plotDataFeat[,i]))),
				sum(t(t(plotDataFam[,i]))))
  if (log=="y" && ! caseStudies[i] =="GPL") {
	maxY=maxY*10 # y axis must be longer, because it is logarithmic
  } else if (caseStudies[i] =="Violet") {
	maxY=maxY*1.1
  } else {
	maxY=maxY*1.2
  }
  yLimits=c(0,maxY)
  if (paintLog) yLimits[1]=0.01
  xLimits=c(1,4.5)
#  if (caseStudies[i]=="Violet") {
#    plot.new() # insert an empty plot to allow for violet's large y axis labels
#  }
  # c(bottom, left, top, right)
  par(mar=c(1, 5, 0, 2))
  plot.new()
  if (i==1 || i==4 || i==7 || i==10) {
    title(ylab="Time (seconds)          ",cex.lab=1.2, mgp=c(3.2,0,0))
  }
  cat ("barplot1, ylim:")
  print(yLimits)
  par(new=TRUE)
  barplot(t(t(plotData[,i])), #, t(t(as.matrix(c(10000,10000,10000,10000))))
		#beside=TRUE,
		space=c(1.1),
		col=color[1:2],
		
		ylim=yLimits,
		xlim=xLimits,
		xaxt="n",
		yaxt="n",
		log=log,
		cex.lab=1.3,
  )
  if (caseStudies[i] =="Violet") {
	textpos = sum(t(t(plotData[,i])))
	text(x=c(1.64), y=c(textpos), labels=c("x"), col=textcolor, cex=2)
  }
  cat ("barplot2, ylim:")
  print(yLimits)
  par(new=TRUE)
  barplot(t(t(plotDataFeat[,i])),
          #beside=TRUE,
          space=c(2.5),
          col=color[1:2],
          ylim=yLimits,
          xlim=xLimits,
          xaxt="n",
          log=log,
          yaxt="n",
  )
  cat ("barplot3, ylim:")
  print(yLimits)
  par(new=TRUE)
  barplot(t(t(plotDataFam[,i])),
          #beside=TRUE,
          space=c(4),
          col=color[1:2],
          ylim=yLimits,
          xlim=xLimits,
          xaxt="n",
          log=log,
          yaxt="n",
  )
  axis(1, tick=FALSE, at=c(1.1,2.5,4)+0.5,labels=c("PB","FT","FM"), cex.axis=1, mgp=c(3,0,0))
	if (par("ylog")) {
		# 10er potenzen falls die achse logarithmisch ist
		aty <- exp(log(10)*seq(log10(yLimits[1]), log10(par("yaxp")[2]),by=1))
	} else {
		# sonst die Skala vom Plot übernehmen
		aty <- seq(yLimits[1], par("yaxp")[2], (par("yaxp")[2] - par("yaxp")[1])/par("yaxp")[3])
	}
	cat ("axis2, aty:")
	print(aty)
	#big.mark is the thousand-seperator
	axis(2, at=aty, labels=formatC(aty, big.mark=" ",digits = 2, format="fg"), hadj=0.9, cex.axis=1,cex.lab=3, las=2)
  
  xLimits=c(1,4)
  maxYP2 = max(sum(t(t(plotDataFeat_wBytecodeComp[,i]))),
  			sum(t(t(plotDataFam_noOpt[,i]))))
  cat ("maxYP2:")
  print(plotDataFam_noOpt[,i])
  if (log=="y" && ! caseStudies[i] =="GPL") {
	maxYP2=maxYP2*10 # y axis must be longer, because it is logarithmic
  } else if (caseStudies[i] =="Violet") {
	maxYP2=maxYP2*1.1
  } else {
	maxYP2=maxYP2*1.2
  }
  yLimits=c(0,maxYP2)
  # c(bottom, left, top, right)
  par(mar=c(1, 2, 0, 3))
  plot.new()
  cat ("barplotNext1, ylim:")
  print(yLimits)
  par(new=TRUE)
  barplot(t(t(plotDataFeat_wBytecodeComp[,i])),
          #beside=TRUE,
          space=c(1.1),
          col=color[1:3],
          ylim=yLimits,
          xlim=xLimits,
          xaxt="n",
          log=log,
          yaxt="n",
  )
  par(new=TRUE)
  if (caseStudies[i] =="Violet") {
	textpos = sum(t(t(plotDataFeat_wBytecodeComp[,i])))+2500
	text(x=c(1.6), y=c(textpos), labels=c("x"), col=textcolor, cex=2)
  }
  cat ("barplotNext2, ylim:")
  print(yLimits)
  par(new=TRUE)
  barplot(t(t(plotDataFam_noOpt[,i])),
          #beside=TRUE,
          space=c(2.5),
          col=color[1:2],
          ylim=yLimits,
          xlim=xLimits,
          xaxt="n",
          log=log,
          yaxt="n",
  )
  axis(1, tick=FALSE, at=c(1.05,2.6)+0.5,labels=c("FT*","FM*"), cex.axis=1, mgp=c(3,0,0))
	if (par("ylog")) {
		# 10er potenzen falls die achse logarithmisch ist
		aty <- exp(log(10)*seq(log10(yLimits[1]), log10(par("yaxp")[2]),by=1))
	} else {
		# sonst die Skala vom Plot übernehmen
		aty <- seq(yLimits[1], par("yaxp")[2], (par("yaxp")[2] - par("yaxp")[1])/par("yaxp")[3])
	}
	#big.mark is the thousand-seperator
	axis(2, at=aty, labels=formatC(aty, big.mark=" ",digits = 2, format="fg"), hadj=0.9, cex.axis=1,cex.lab=3, las=2)
	
	#title
	# c(bottom, left, top, right)
	par(mar=c(0,0,0,0)) 
	plot.new()
	if(caseStudies[i]=="Graph") {
		title(main="GraphLib",line=-2.5)
	} else {
		title(main=caseStudies[i],line=-2.5)
	}
}

# c(bottom, left, top, right)
par(mar=c(0,0,0,0)) 
plot.new()
legend(x=-0.04,y=1,
       c(
       "PB   Product-based",
       "FT    Feature-based",
       "FM   Family-based",
       "FT*   Feature-product-based",
       "FM*  Family-based (no caching)"),
       inset = 0, cex=1)
       
par(mar=c(0,0,0,0)) 
legend(x=-0.04,y=0.515,
       c('Setup',
       'Checking',
         'Compose'),
       inset = 0, fill=color, cex=1)

warnings()
dev.off()

#######################################################
## Output LaTeX table with measurement results
cat("Results LaTeX table for Product-Based (Setup, Check, Total) | Feature-Based (Setup, Check, Total, PB/FT) | Family-Based (Setup, Check, Total, PB/FM, FT/FM)\n")

strategyDataRowLatex_prod <- function(splNr) {
  violetMarker <- ""
  if (caseStudies[splNr] == "Violet") {
    violetMarker <- "\\textsuperscript{\\sffamily{}X}"
  } else {
    violetMarker <- ""
  }
  paste("",  round(plotData[1,splNr], 1), violetMarker, " & ",   round(plotData[2,splNr], 1), violetMarker, " & ",  round(plotData[1,splNr] + plotData[2,splNr], 1), violetMarker, sep = "")
}
strategyDataRowLatex_feat <- function(splNr) {
  violetMarker <- ""
  if (caseStudies[splNr] == "Violet") {
    violetMarker <- "\\textsuperscript{\\sffamily{}X}"
  } else {
    violetMarker <- ""
  }
  paste("",  round(plotDataFeat[1,splNr], 1), " & ",   round(plotDataFeat[2,splNr], 1), " & ",  round(plotDataFeat[1,splNr] + plotDataFeat[2,splNr], 1), " & ", 
		round((plotData[1,splNr]+plotData[2,splNr])/(plotDataFeat[1,splNr]+plotDataFeat[2,splNr]), 1), violetMarker, sep = "")
}
strategyDataRowLatex_fam <- function(splNr) {
  violetMarker <- ""
  if (caseStudies[splNr] == "Violet") {
    violetMarker <- "\\textsuperscript{\\sffamily{}X}"
  } else {
    violetMarker <- ""
  }
  paste("",  round(plotDataFam[1,splNr], 1), " & ",   round(plotDataFam[2,splNr], 1), " & ",  round(plotDataFam[1,splNr] + plotDataFam[2,splNr], 1),
		" & ", round((plotData[1,splNr]+plotData[2,splNr])/(plotDataFam[1,splNr]+plotDataFam[2,splNr]), 1), violetMarker,
		" & ", round((plotDataFeat[1,splNr]+plotDataFeat[2,splNr])/(plotDataFam[1,splNr]+plotDataFam[2,splNr]), 1), sep = "")
}
strategyDataRowLatex_feat_wBytecodeComp <- function(splNr) {
  violetMarker <- ""
  if (caseStudies[splNr] == "Violet") {
    violetMarker <- "\\textsuperscript{\\sffamily{}X}"
  } else {
    violetMarker <- ""
  }
  paste("",  round(plotDataFeat_wBytecodeComp[1,splNr] + plotDataFeat_wBytecodeComp[2,splNr] + plotDataFeat_wBytecodeComp[3,splNr], 1), violetMarker, sep = "")
}
strategyDataRowLatex_fam_noOpt <- function(splNr) {
  violetMarker <- ""
  if (caseStudies[splNr] == "Violet") {
    violetMarker <- "\\textsuperscript{\\sffamily{}X}"
  } else {
    violetMarker <- ""
  }
  paste("",  round((plotDataFam_noOpt[1,splNr]+plotDataFam_noOpt[2,splNr]), 1), sep = "")
}
for (i in 1:length(caseStudies)) {
  cat(caseStudies[i], " & ", strategyDataRowLatex_prod(i), " & ", strategyDataRowLatex_feat(i), " & ", strategyDataRowLatex_fam(i), " & ", strategyDataRowLatex_feat_wBytecodeComp(i), " & ", strategyDataRowLatex_fam_noOpt(i), "\\\\\n", sep = "")
}
cat("\n")
#######################################################
## Output CSV table with measurement results
cat("Results CSV table for Product-Based | Feature-Based | Family-Based\n")
cat("Name,PDBSetup,PDBCheck,PDBTotal,FTBSetup,FTBCheck,FTBTotal,FMBSetup,FMBCheck,FMBTotal\n")
strategyDataRow <- function(data, splNr) {
  paste(data[1,splNr], ",",  data[2,splNr], ",", data[1,splNr] + data[2,splNr], sep = "")
}
for (i in 1:length(caseStudies)) {
  cat(caseStudies[i], ",", strategyDataRow(plotData, i), ",", strategyDataRow(plotDataFeat, i), ",", strategyDataRow(plotDataFam, i), "\n", sep = "")
}
cat("\n")

#######################################################
## plot sum over all case studies

plotDataSum <- matrix(nrow=1, ncol=3)
plotDataSum[1,1] = sum(plotData)
plotDataSum[1,2] = sum(plotDataFeat)
plotDataSum[1,3] = sum(plotDataFam)
print(plotDataSum)

colorSum <- c(color[1], color[3], color[5])
names = c('product-based', 'feature-based', 'family-based')

if (!draft) pdf(file=paste("plot_int_sum",".pdf",sep=""), width=8, height=3, onefile=TRUE, paper="special") 

logX = "" # "x" for logarithmic x-scale, "" otherwise
xLimitsSum=c(0,max(plotDataSum))
if (logX == "x") {
 xLimitsSum[1] = 1
}

par(las=2) # make label text perpendicular to axis
par(mar=c(5,8,0,2)) # increase y-axis margin.
barplot(plotDataSum,
        beside=TRUE,
        space=c(0.1, 0.1),
        col=colorSum[1:3],
        names.arg = names,
        horiz=TRUE,
        ylim=c(0,4)
)
warnings()
if (!draft) dev.off()
