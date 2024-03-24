# COMP4057_Assignment1_vaccination

---
## how to connect to COMP in home
1. open a cmd & write `putty -l f3219041 -L 22222:158.182.9.40:22 faith.comp.hkbu.edu.hk`
2. open another cmd & write `putty -l f3219041 -P 22222 127.0.0.1`

## TODO
```
src/23219041_Assignment1/Q4/runQ4.sh
hadoop fs -copyFromLocal vaccination-rates-over-time-by-age-v2.txt ~
hadoop fs -rm -r ~/Vaccination_Q2
hadoop jar Vaccination_Q1.jar Vaccination_Q1 ~/vaccination-rates-over-time-by-age.txt ~/Vaccination_Q1
hadoop fs -cat ~/Vaccination_Q2/* > ~/src/Assignment1/Q2/vaccination_Q2.txt
```