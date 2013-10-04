caseStudyNames=("ZipMe" "EPL" "GPL" "Graph" "GUIDSL" "Notepad" "PKJab" "Prevayler" "Raroscope" "Sudoku" "Violet" "TankWar")


rm -r resultBackup
mkdir resultBackup
for csname in "${caseStudyNames[@]}"
do
	mkdir resultBackup/$csname
	mkdir resultBackup/$csname/FeatureConfigs
	mkdir resultBackup/$csname/products
	cp $csname/inttimetypechecker.csv resultBackup/$csname/inttimetypechecker.csv
	cp $csname/inttimetypechecker_featurebased.csv resultBackup/$csname/inttimetypechecker_featurebased.csv
	cp $csname/exttimetypechecker.csv resultBackup/$csname/exttimetypechecker.csv
	cp $csname/exttimetypechecker_featurebased.csv resultBackup/$csname/exttimetypechecker_featurebased.csv
	
	mkdir resultBackup/$csname/family/
	cp $csname/familyerrout.txt resultBackup/$csname/family/familyerrout.txt
	cp $csname/familystdout.txt resultBackup/$csname/family/familystdout.txt
	
	for variantdir in $csname/FeatureConfigs/Config*;
	do
		mkdir resultBackup/$variantdir
		cp $variantdir/stdout.txt resultBackup/$variantdir
		cp $variantdir/errout.txt resultBackup/$variantdir
	done
	for variantdir in $csname/products/Variant*;
	do
		mkdir resultBackup/$variantdir
		cp $variantdir/stdout.txt resultBackup/$variantdir
		cp $variantdir/errout.txt resultBackup/$variantdir
	done
	
done