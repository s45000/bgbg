import pymysql
from datetime import datetime
#import pandas as pd

hour = None
start = datetime.now().strftime('%Y년%m월%d일%H시 BGtable')
while(True):
    if(hour != datetime.now().hour):
        hour = datetime.now().hour

        conn = pymysql.connect(host='116.89.189.17', user = 'dip', password = 'korenpass', db = 'BGdb', charset='utf8')
        cur = conn.cursor()
        sql = "select place, nowN, updateTime from BGtable where isActive = 1"
        cur.execute(sql)

        select = list(cur.fetchall())
        nowT = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        for DBinfo in select:
            file = open("/var/www/html/"+DBinfo[0]+".txt","a")
            file.write("기록시간 : "+nowT+"\n")
            data = DBinfo[0]+"\t 인원 : "+str(DBinfo[1])+"\t 최근 업데이트 : "+str(DBinfo[2])+"\n\n"
            file.write(data)
            file.close()

        conn.commit()
        conn.close()
