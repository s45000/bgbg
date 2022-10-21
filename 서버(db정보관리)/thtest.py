import threading
import time
import multiprocessing

def main():
    start = time.time()
    ps = []
    for i in range(4):
        p = multiprocessing.Process(target = work, args = (i,))
        p.start()
        ps.append(p)
    for p in ps:
        p.join()
    end = time.time()
    print("time : %f sec" % (end - start))

#    t = threading.Thread(target = myprint, args = (1,))
#    t.start()
#    t = threading.Thread(target = myprint, args = (2,))
#    t.start()

def myprint(n):
    while True:
        print(n)
        time.sleep(0.5)

def work(name):
    result = 0
    for i in range(40000000):
        result += 1
    print('%s done' % name)

if __name__ == "__main__":
    main()

