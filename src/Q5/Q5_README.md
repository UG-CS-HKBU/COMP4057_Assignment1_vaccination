# Instruction
## step
1. make a directory `~/src/23219041_Assignment1/Q5` for store jar & text file
2. upload the `vaccination-rates-over-time-by-age-v2.txt`, `runQ5.sh`[assets folder] and `Vaccination_Q5.jar`[out folder]
3. Give `runQ5.sh` execute permission
4. run the `runQ5.sh`. If the shell program is not working, follow the step below
```
hadoop fs -rm -r ~/Vaccination_Q5
hadoop jar ~/src/23219041_Assignment1/Q5/Vaccination_Q5.jar Vaccination_Q5 ~/vaccination-rates-over-time-by-age-v2.txt ~/Vaccination_Q5
hadoop fs -cat ~/Vaccination_Q5/* > ~/src/23219041_Assignment1/Q5/Vaccination_Q5.txt
```