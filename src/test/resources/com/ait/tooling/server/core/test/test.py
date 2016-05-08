import sys
x = 1
def increment_x():
	global x
	x = x + 1
	print "Python value is now %d" % x
def testargs(a, b):
    print "Python a is now ", a
    print "Python b is now ", b