import sys
x = 1
def increment_x():
	global x
	x = x + 1
	print "Python value is now %d" % x