### Hashcat wrapper

```
$ ./wrapcat.py --help
usage: wrapcat.py [-h] --bin bin --hash file --mode mode [-d file]
                  [-D directory] [-r file] [-R directory] [--debug] [--log]
                  [--output file]

Hashcat wrapper

optional arguments:
  -h, --help            show this help message and exit
  --bin bin, -b bin     hashcat binary
  --hash file           hashfile
  --mode mode, -m mode  hash mode
  -d file               dictionary file
  -D directory          dictionaries directory
  -r file               ruleset file
  -R directory          rulesets directory
  --debug               enable debug logging
  --log                 logging to file app.log
  --output file, -o file
                        output to csv file

Results expliantion:
  K1 - (recoverd hashes / total hashes) / total variants
  K2 - recoverd hashes / total hashes

Author: @tsma11
```

##### Example usage
```
$ ./wrapcat.py -b ../hashcat/hashcat -m 0 --hash ../../research_md5 -D all_dict/ -R ../../rules/cap/ --debug --log -o all_dict_with_cap.csv
```