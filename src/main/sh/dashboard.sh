#!/bin/bash

echo 'Processed:'
grep -i 'Process file' clc.log  | wc -l

echo ''
echo 'Exceptions:'
grep -i exception clc.log | wc -l

echo ''
echo 'Skipped Lines:'
grep -i skipping clc.log | wc -l
