# Instruction
## step
1. make a directory `~/src/23219041_Assignment1/Q3` for store jar & text file
2. upload the `vaccination-rates-over-time-by-age-v2.txt`, `runQ3.sh`[assets folder] and `Vaccination_Q3.jar`[out folder]
3. Give `runQ3.sh` execute permission
4. run the `runQ3.sh`. If the shell program is not working, follow the step below
```
hadoop fs -rm -r ~/Vaccination_Q3
hadoop jar ~/src/23219041_Assignment1/Q3/Vaccination_Q3.jar Vaccination_Q3 ~/vaccination-rates-over-time-by-age-v2.txt ~/Vaccination_Q3
hadoop fs -cat ~/Vaccination_Q3/* > ~/src/23219041_Assignment1/Q3/Vaccination_Q3.txt
```