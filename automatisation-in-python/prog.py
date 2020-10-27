invocations = list()

with open('message.txt') as f:
    lines = f.readlines()


for line in lines :
	if line not in invocations :
		invocations.append(line)
	

outF = open("sortie.txt", "w")
for line in invocations :
	outF.write(line)
	print("Ligne : {}".format(line))