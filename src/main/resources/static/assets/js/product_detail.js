/**
 * /src/main/resources/static/assets/js/product_detail.js
 */

// HTML에서 정의된 상품 정보 변수를 잘 로드하고 있는지 확인하기 위해 로그로 출력
console.group("상품 정보");
console.log(productData);
console.groupEnd();

// 현재 선택된 옵션 조합을 생성하기 위한 빈 객체
let selectedCombinations = [];

// ====================================================
// 상품 상세 정보 탭 이벤트
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


// ====================================================
// 상품 옵션 선택 이벤트
// ====================================================
document.querySelectorAll(".option-value").forEach((v, i) => {
    v.addEventListener("click", (e) => {
        const current = e.currentTarget;

        // 같은 그룹(컬러, 사이즈 등)의 다른 옵션들에서 selected 클래스 제거
        const family = current.parentElement.querySelectorAll('.option-value');
        family.forEach((vv, ii) => vv.classList.remove('selected'));

        // 클릭된 옵션에 selected 클래스 추가하여 선택 상태 표시
        current.classList.add('selected');

        // 옵션 그룹 단위 객체
        const optionGroups = document.querySelectorAll('.option-group');

        const optionChoice = {};

        // 각 옵션 그룹(컬러, 사이즈 등)을 순회
        optionGroups.forEach((vv, ii) => {
            // 각 그룹별로 선택된 항목 가져오기
            const selectedOption = vv.querySelector('.option-value.selected');
            //console.log(selectedOption);

            // 선택된 항목의 옵션번호,옵션명,옵션값 가져오기
            if (selectedOption) {
                const optionName = selectedOption.getAttribute('data-option-name');
                const optionValue = selectedOption.innerHTML;

                optionChoice[optionName] = optionValue;
            }
        });

        //console.log(optionChoice);

        // 선택된 옵션 정보의 길이가 전체 옵션 그룹과 같지 않다면 처리 중단
        if (Object.keys(optionChoice).length !== optionGroups.length) {
            return;
        }

        // 선택된 옵션 정보의 길이와 전체 옵션 그룹 길이가 같다면 선택된 옵션으로 등록하기 위해 수량과 가격 정보 추가
        // 가격은 productData에서 discount가 있다면 할인율을 적용하고, 그렇지 않다면 원래 가격을 사용
        let optionPrice = productData.price;

        if (productData.discount > 0) {
            optionPrice -= parseInt(productData.price * productData.discount / 100)
        }

        optionChoice.price = optionPrice; // 가격 정보 추가
        optionChoice.quantity = 1; // 기본 수량 1
        //console.log(optionChoice);


        // selectedCombinations에 price와 quantity를 제외한 현재 선택된 옵션 조합이 있는지 확인
        const existingCombinationIndex = selectedCombinations.findIndex(combination => {
            console.log(combination);
            return Object.keys(optionChoice).every(key => {
                return key === 'price' || key === 'quantity' || combination[key] === optionChoice[key];
            });
        });

        // 이미 존재하는 조합이 있다면 해당 조합의 수량을 증가시키고, 그렇지 않다면 새로운 조합으로 추가
        if (existingCombinationIndex !== -1) {
            // 기존 조합의 수량을 증가
            selectedCombinations[existingCombinationIndex].quantity += 1;
        } else {
            // 새로운 조합으로 추가
            const newCombination = {
                id: Date.now(), // 고유 ID 생성 (예: 현재 시간)
                ...optionChoice
            };
            selectedCombinations.push(newCombination);
        }

        updateCombinationDisplay();
    });
});

// ====================================================
// 선택된 옵션 조합에 대한 UI 갱신 함수
// ====================================================
function updateCombinationDisplay() {
    console.log(selectedCombinations);

    const combinationList = document.querySelector('#combinationList');
    combinationList.innerHTML = '';


    if (selectedCombinations.length === 0) {
        combinationList.style.display = 'none';
        return;
    }

    combinationList.style.display = 'block';

    // 옵션 정보만큼 순회
    selectedCombinations.forEach((vv, ii) => {
        // 옵션정보 예시 --> {id: 1754988069673, 컬러: '데님', 사이즈: '235', price: 33600, quantity: 1}
        // 메인 컨테이너 생성
        const combinationItem = document.createElement('div');
        combinationItem.className = 'combination-item';
        combinationItem.setAttribute('data-combination-id', vv.id);

        // 조합 정보 영역 생성
        const combinationInfo = document.createElement('div');
        combinationInfo.className = 'combination-info';

        // 옵션 정보 표시
        const combinationOptions = document.createElement('div');
        combinationOptions.className = 'combination-options';

        // 옵션 정보를 순회하며 표시
        Object.keys(vv).forEach((key) => {
            console.log(key);
            if (key !== 'id' && key !== 'price' && key !== 'quantity') {
                const optionItem = document.createElement('span');
                optionItem.className = 'combination-option';
                optionItem.textContent = `${key}: ${vv[key]}`;
                combinationOptions.appendChild(optionItem);
            }
        });

        // 가격 정보 표시
        const combinationPrice = document.createElement('div');
        combinationPrice.className = 'combination-price';
        combinationPrice.textContent = `₩${(vv.price * vv.quantity).toLocaleString()}`;

        // 조합 정보에 옵션과 가격 추가
        combinationInfo.appendChild(combinationOptions);
        combinationInfo.appendChild(combinationPrice);

        // 수량 조절 영역 생성
        const combinationQuantity = document.createElement('div');
        combinationQuantity.className = 'combination-quantity';

        // 수량 감소 버튼
        const decreaseBtn = document.createElement('button');
        decreaseBtn.type = 'button';
        decreaseBtn.className = 'combination-quantity-btn';
        decreaseBtn.textContent = '-';
        decreaseBtn.addEventListener('click', () => changeCombinationQuantity(vv.id, -1));

        // 수량 입력 필드
        const quantityInput = document.createElement('input');
        quantityInput.type = 'number';
        quantityInput.className = 'combination-quantity-input';
        quantityInput.value = vv.quantity;
        quantityInput.min = '1';
        quantityInput.addEventListener('change', (e) => setCombinationQuantity(vv.id, e.target.value));

        // 수량 증가 버튼
        const increaseBtn = document.createElement('button');
        increaseBtn.type = 'button';
        increaseBtn.className = 'combination-quantity-btn';
        increaseBtn.textContent = '+';
        increaseBtn.addEventListener('click', () => changeCombinationQuantity(vv.id, 1));

        // 수량 조절 영역에 버튼들 추가
        combinationQuantity.appendChild(decreaseBtn);
        combinationQuantity.appendChild(quantityInput);
        combinationQuantity.appendChild(increaseBtn);

        // 삭제 버튼 생성
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.className = 'combination-remove';
        removeBtn.textContent = '삭제';
        removeBtn.addEventListener('click', () => removeCombination(vv.id));

        // 메인 컨테이너에 모든 요소 추가
        combinationItem.appendChild(combinationInfo);
        combinationItem.appendChild(combinationQuantity);
        combinationItem.appendChild(removeBtn);

        // 조합 리스트에 추가
        combinationList.appendChild(combinationItem);
    });
}

// ====================================================
// 특정 조합을 목록에서 제거
// ====================================================
function removeCombination(combinationId) {
    // 해당 ID를 제외한 조합들로 배열 재구성
    selectedCombinations = selectedCombinations.filter(combo => combo.id !== combinationId);

    updateCombinationDisplay();
};