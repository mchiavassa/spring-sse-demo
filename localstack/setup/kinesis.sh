#!/bin/bash

awslocal kinesis create-stream --stream-name sse-demo-stream --shard-count 1

printf 'Stream created'
