<video autoplay = 1 loop = 1 src = "disd.mp4" width = "1440" height = "860" ></video>
<div style="text-align: right; padding-right: 50px;">
            <span id="clock" style="color:white; font-size: 40px;">clock</span>
        </div>
        <script>
        var Target = document.getElementById("clock");
        function clock() {
            var time = new Date();

            var year = time.getYear();
            var month = time.getMonth();
            var date = time.getDate();
            var day = time.getDay();

            var hours = time.getHours();
            var minutes = time.getMinutes();
            var seconds = time.getSeconds();

            Target.innerText = 
            `${year + 1900}년 ${month + 1}월 ${date}일 ` +
            `${hours < 10 ? `0${hours}` : hours}:${minutes < 10 ? `0${minutes}` : minutes}:${seconds < 10 ? `0${seconds}` : seconds}`;
                
        }
        clock();
        setInterval(clock, 1000);
