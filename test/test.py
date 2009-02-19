#!/usr/bin/env python
import os

class Test:
    def findDirs(dir):
        return [x  for x in os.listdir(dir) if x.startswith('test')]


