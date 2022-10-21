import pymysql
from datetime import datetime
	
conn = None
cur = None

sql = ""

conn = pymysql.connect(host='116.89.189.17', user = 'dip', password = 'korenpass', db = 'BGdb', charset='utf8')
cur = conn.cursor()

#nowT = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
#sql  ="update BGtable set nowN = 15 where place = '양주시보건소'"
sql = "update BGtable set id = 'udang' where place = '경동대학교 우당관'"
cur.execute(sql)

conn.commit()

conn.close()
