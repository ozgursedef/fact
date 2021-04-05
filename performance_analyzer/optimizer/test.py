from gekko import GEKKO
import numpy as np

#Initialize Model
m = GEKKO(remote=True)

#help(m)

#define parameter
max = m.Param(value=3008)
min = m.Param(value=128)
step = m.Param(value=64)
limit = m.Param(value=400)
a = m.Param(value=1265)
b = m.Param(value=-0.19)

#initialize variables
x = m.Var()
w = m.Var()

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

#solve simulation
m.solve()

print('')
print('Result')
print('x: ' + str(x.value))

