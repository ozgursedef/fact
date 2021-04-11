from gekko import GEKKO
import numpy as np
import sys

#Initialize Model
m = GEKKO(remote=True)

#help(m)

#define parameter
max = m.Const(value=3008)
min = m.Const(value=128)
step = m.Const(value=64)
limit = m.Const(value=400)
a = m.Const(value=sys.argv[2])
b = m.Const(value=sys.argv[4])

#initialize variables
x = m.CV(integer=True)
w = m.Var(integer=True)

#lower bounds
x.lower = min
w.lower = 0

#upper bounds
x.upper = max

#equations  
m.Equation(x - w * step == 0)
m.Equation(a*x**b<=limit)

#objective
#min by default
m.Obj(x*a*x**b)

#set global options
m.options.IMODE = 3 #steady state optimization
m.options.SOLVER = 1 #set integer non-linear programming

#solve simulation
m.solve()

print('')
print('Result')
print('x: ' + str(x.value))