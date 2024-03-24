# Instruction
## step
1. make a directory `~/src/23219041_Assignment1/Q2` for store jar & text file
2. upload the `vaccination-rates-over-time-by-age-v2.txt`, `runQ2.sh`[assets folder] and `Vaccination_Q2.jar`[out folder]
3. Give `runQ2.sh` execute permission
4. run the `runQ2.sh`. If the shell program is not working, follow the step below
```
hadoop fs -copyFromLocal ~/src/23219041_Assignment1/Q2/vaccination-rates-over-time-by-age-v2.txt ~
hadoop fs -rm -r ~/Vaccination_Q2
hadoop jar ~/src/23219041_Assignment1/Q2/Vaccination_Q2.jar Vaccination_Q2 ~/vaccination-rates-over-time-by-age-v2.txt ~/Vaccination_Q2
hadoop fs -cat ~/Vaccination_Q2/* > ~/src/23219041_Assignment1/Q2/Vaccination_Q2.txt
```