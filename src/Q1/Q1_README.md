# Instruction
## step
1. make a directory `~/src/23219041_Assignment1/Q1` for store jar & text file
2. upload the `vaccination-rates-over-time-by-age.txt`, `runQ1.sh`[assets folder] and `Vaccination_Q1.jar`[out folder]
3. Give `runQ1.sh` execute permission
4. run the `runQ1.sh`. If the shell program is not working, follow the step below
```
hadoop fs -copyFromLocal ~/src/23219041_Assignment1/Q1/vaccination-rates-over-time-by-age.txt ~
hadoop fs -rm -r ~/Vaccination_Q1
hadoop jar ~/src/23219041_Assignment1/Q1/Vaccination_Q1.jar Vaccination_Q1 ~/vaccination-rates-over-time-by-age.txt ~/Vaccination_Q1
hadoop fs -cat ~/Vaccination_Q1/* > ~/src/23219041_Assignment1/Q2/vaccination-rates-over-time-by-age-v2.txt
```