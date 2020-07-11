import random
preambles = {}

def create_preamble():
    for i in range(64):
        preambles.update({i+1:'preamble ' + str(i+1)})


def random_preamble_selection(contention_based):
    if(contention_based):
        preamble = random.randint(1,64)
        return preamble
    else:
        return 0

create_preamble()

def UE_1():
    preamble = random_preamble_selection(True)
