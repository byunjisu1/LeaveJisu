var nameEl = document.getElementById('chose');

// 1. 데이터 생성 (년도와 월)
var years = [];
var months = [];

// 년도 생성 (예: 2000년 ~ 2026년)
for (var i = 2000; i <= 2026; i++) {
    years.push({ text: i + '년', value: i });
}

// 월 생성 (1월 ~ 12월)
for (var j = 1; j <= 12; j++) {
	var monthValue = String(j).padStart(2, '0'); 
    months.push({ text: monthValue + '월', value: monthValue });
    
}

// 2. 피커 초기화
var betterPicker = new Picker({
    data: [years, months], // 년도와 월 데이터만 넣음 (2단 구조)
    selectedIndex: [years.length - 1, 0], // 마지막 년도, 1월을 기본값으로
    title: '날짜 선택'
});

// 3. 선택 완료 시 이벤트
betterPicker.on('picker.select', function (selectedVal, selectedIndex) {
    var yearText = years[selectedIndex[0]].text;
    var monthText = months[selectedIndex[1]].text;
    nameEl.innerText = yearText + ' ' + monthText;
});

// 클릭 시 피커 노출
nameEl.addEventListener('click', function () {
    betterPicker.show();
});