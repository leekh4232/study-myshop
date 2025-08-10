/**
 * /src/main/resources/static/assets/js/product_detail.js
 */

// HTML에서 정의된 상품 정보 변수를 잘 로드하고 있는지 확인하기 위해 로그로 출력
console.group("상품 정보");
console.log(productData);
console.groupEnd();

// ====================================================
// UI 관련 이벤트
// ====================================================
document.querySelectorAll(".tab-header").forEach((v, i) => {
    v.addEventListener("click", (e) => {
        // 모든 탭 헤더에서 active 클래스 제거
        const tabHeaders = document.querySelectorAll('.tab-header');
        tabHeaders.forEach(header => header.classList.remove('active'));

        // 모든 탭 패인에서 active 클래스 제거
        const tabPanes = document.querySelectorAll('.tab-pane');
        tabPanes.forEach(pane => pane.classList.remove('active'));

        // 클릭된 탭 헤더에 active 클래스 추가
        e.currentTarget.classList.add('active');

        // 클릭된 탭의 data-target 속성 가져오기
        const tabName = e.currentTarget.dataset.target;

        // 해당 탭 패인에 active 클래스 추가하여 표시
        document.getElementById(`${tabName}-tab`).classList.add('active');
    });
});