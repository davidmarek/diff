#!/usr/bin/env python
import os

class Test:
    def findDirs(self, dir):
        return [x  for x in os.listdir(dir) if x.startswith('test_')]
    
    def testDiff(self, dir):
        files = os.listdir(dir)
        assert len(files) == 2
        os.chdir(dir)
        self.testNormalDiff(files[0],files[1])
        self.testUnifiedDiff(files[0],files[1],0)
        self.testUnifiedDiff(files[0],files[1],1)
        self.testUnifiedDiff(files[0],files[1],3)
        self.testUnifiedDiff(files[0],files[1],5)
        self.testUnifiedDiff(files[0],files[1],10)
        os.chdir('..')
    
    def comparePatchedFiles(self, file1, file2,msg):
        os.popen(" ".join(["patch -b ",file1,"< mujdiff.out"]))
        if os.popen(" ".join(["diff",file1,file2])).read() == "":
            print " ".join(["Test",file1,file2,msg,": OK"])
        else:
            print " ".join(["Test",file1,file2,msg,": Failed"])

    def clean(self, file1):
        os.remove("mujdiff.out")
        try:
            os.rename("".join([file1,".orig"]), file1)
        except OSError, e:
            pass

    def testNormalDiff(self, file1, file2):
        # Vytvoreni diffu
        os.popen(" ".join(['java -jar "../../dist/Diff.jar"',file1,file2," > mujdiff.out"]))
        # Aplikace patche a kontrola
        self.comparePatchedFiles(file1, file2, "normal diff")
        # Uklid
        self.clean(file1)

    def testUnifiedDiff(self, file1, file2, context):
        os.popen(" ".join(['java -jar "../../dist/Diff.jar" -U', str(context), file1, file2, '> mujdiff.out']))
        self.comparePatchedFiles(file1,file2, "unified diff %d"%context)
        self.clean(file1)

    def testDirs(self):
        for d in self.findDirs('.'):
            self.testDiff(d)
    
if __name__ == '__main__':
    t = Test()
    t.testDirs()
    
