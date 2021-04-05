from gekko import GEKKO
import numpy as np

#Initialize Model
m = GEKKO(remote=True)

#help(m)

#define parameter
max = m.Const(value=3008)
min = m.Const(value=128)
step = m.Const(value=64)
limit = m.Const(value=400)
a = m.Const(value=1265)
b = m.Const(value=-0.19)

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
m.options.SOLVER = 1

#solve simulation
m.solve()

print('')
print('Result')
print('x: ' + str(x.value))