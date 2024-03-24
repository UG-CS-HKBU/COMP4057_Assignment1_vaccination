# Instruction
## step
1. make a directory `~/src/23219041_Assignment1/Q4` for store jar & text file
2. upload the `vaccination-rates-over-time-by-age-v2.txt`, `runQ4.sh`[assets folder] and `Vaccination_Q4.jar`[out folder]
3. Give `runQ4.sh` execute permission
4. run the `runQ4.sh`. If the shell program is not working, follow the step below
```
hadoop fs -rm -r ~/Vaccination_Q4
hadoop jar ~/src/23219041_Assignment1/Q4/Vaccination_Q4.jar Vaccination_Q4 ~/vaccination-rates-over-time-by-age-v2.txt ~/Vaccination_Q4
hadoop fs -cat ~/Vaccination_Q4/* > ~/src/23219041_Assignment1/Q4/Vaccination_Q4.txt
```