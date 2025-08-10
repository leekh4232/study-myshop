// ====================================================
// 전역 변수 정의 영역
// ====================================================

// 선택된 옵션 조합들을 저장하는 배열
let selectedCombinations = [];

// 조합에 고유 ID를 부여하기 위한 카운터
let combinationIdCounter = 0;

// 상품 가격 정보 (전역으로 설정될 예정)
let productPrice = 0;           // 원가격
let productDiscount = 0;        // 할인율
let finalPrice = 0;             // 최종 판매가격

// ====================================================
// 옵션 선택 관련 함수들
// ====================================================

/**
 * 상품 옵션 선택 처리 함수
 * @param {HTMLElement} element - 클릭된 옵션 요소
 */
const selectOption = (element) => {
    // 같은 그룹(컬러, 사이즈 등)의 다른 옵션들에서 selected 클래스 제거
    const optionGroup = element.parentElement;
    const siblings = optionGroup.querySelectorAll('.option-value');
    siblings.forEach(sibling => sibling.classList.remove('selected'));

    // 클릭된 옵션에 selected 클래스 추가하여 선택 상태 표시
    element.classList.add('selected');

    // 조합 추가 버튼 활성화/비활성화 상태 업데이트
    updateAddCombinationButton();
};

/**
 * 조합 추가 버튼의 활성화 상태와 텍스트를 업데이트
 */
const updateAddCombinationButton = () => {
    const selectedOptions = getSelectedOptions();
    const addBtn = document.getElementById('addCombinationBtn');

    // 전체 옵션 그룹 수 계산
    const totalOptionGroups = document.querySelectorAll('.option-group').length;

    // 선택된 옵션 그룹 수 계산
    const selectedOptionGroups = Object.keys(selectedOptions).length;

    // 모든 옵션 그룹에서 옵션이 선택되지 않았으면 버튼 비활성화
    if (selectedOptionGroups === 0) {
        addBtn.disabled = true;
        addBtn.textContent = '옵션을 선택해주세요';
    } else if (selectedOptionGroups < totalOptionGroups) {
        // 일부 옵션 그룹만 선택된 경우
        addBtn.disabled = true;
        addBtn.textContent = `모든 옵션을 선택해주세요 (${selectedOptionGroups}/${totalOptionGroups})`;
    } else {
        // 모든 옵션 그룹에서 옵션이 선택된 경우
        addBtn.disabled = false;
        addBtn.textContent = '선택한 옵션 조합 추가 (수량: 1개)';
    }
};

/**
 * 현재 선택된 모든 옵션들을 객체 형태로 반환
 * @returns {Object} 선택된 옵션들의 정보
 */
const getSelectedOptions = () => {
    const selectedOptions = {};
    const optionGroups = document.querySelectorAll('.option-group');

    // 각 옵션 그룹(컬러, 사이즈 등)을 순회
    optionGroups.forEach(group => {
        const label = group.querySelector('.option-label').textContent;
        const selectedOption = group.querySelector('.option-value.selected');

        // 해당 그룹에서 선택된 옵션이 있으면 저장
        if (selectedOption) {
            selectedOptions[label] = {
                id: selectedOption.getAttribute('data-option-id'),
                name: selectedOption.getAttribute('data-option-name'),
                value: selectedOption.textContent
            };
        }
    });

    return selectedOptions;
};

// ====================================================
// 조합 관리 관련 함수들
// ====================================================

/**
 * 선택된 옵션을 조합 목록에 추가
 */
const addCombination = () => {
    const selectedOptions = getSelectedOptions();
    const quantity = 1; // 기본 수량은 1개로 고정

    // 전체 옵션 그룹 수 계산
    const totalOptionGroups = document.querySelectorAll('.option-group').length;

    // 선택된 옵션 그룹 수 계산
    const selectedOptionGroups = Object.keys(selectedOptions).length;

    // 옵션이 선택되지 않았으면 경고 메시지
    if (selectedOptionGroups === 0) {
        alert('옵션을 선택해주세요.');
        return;
    }

    // 모든 옵션이 선택되지 않았으면 경고 메시지
    if (selectedOptionGroups < totalOptionGroups) {
        alert(`모든 옵션을 선택해주세요. (현재 ${selectedOptionGroups}/${totalOptionGroups} 선택됨)`);
        return;
    }

    // 동일한 조합이 이미 있는지 확인하기 위한 문자열 생성
    const optionKeys = Object.keys(selectedOptions).sort();
    const optionString = optionKeys.map(key =>
        `${key}:${selectedOptions[key].value}`
    ).join('|');

    // 기존 조합 목록에서 동일한 조합 찾기
    const existingCombination = selectedCombinations.find(
        combo => combo.optionString === optionString
    );

    if (existingCombination) {
        // 기존 조합이 있으면 수량만 증가
        existingCombination.quantity += quantity;
        updateCombinationDisplay();
    } else {
        // 새로운 조합 생성 및 추가
        const newCombination = {
            id: ++combinationIdCounter,        // 고유 ID
            options: selectedOptions,          // 선택된 옵션들
            optionString,                      // 중복 체크용 문자열
            quantity,                          // 수량
            price: finalPrice                  // 단가
        };

        selectedCombinations.push(newCombination);
        updateCombinationDisplay();
    }

    // 옵션 선택 상태 초기화하여 다음 조합 선택 준비
    resetOptions();
};

/**
 * 옵션 선택 상태를 초기화
 */
const resetOptions = () => {
    // 모든 선택된 옵션에서 selected 클래스 제거
    const selectedOptions = document.querySelectorAll('.option-value.selected');
    selectedOptions.forEach(option => option.classList.remove('selected'));

    // 조합 추가 버튼 상태 업데이트
    updateAddCombinationButton();
};

/**
 * 선택된 조합 목록을 화면에 표시
 */
const updateCombinationDisplay = () => {
    const combinationList = document.getElementById('combinationList');

    // 기존 내용 제거
    combinationList.innerHTML = '';

    // 선택된 조합이 없으면 안내 메시지 표시
    if (selectedCombinations.length === 0) {
        const emptyDiv = document.createElement('div');
        emptyDiv.className = 'empty-combinations';
        emptyDiv.textContent = '옵션을 선택하고 "선택한 옵션 조합 추가" 버튼을 눌러주세요.';
        combinationList.appendChild(emptyDiv);
    } else {
        // 각 조합을 DOM 요소로 생성하여 표시
        selectedCombinations.forEach(combo => {
            // 메인 컨테이너 생성
            const combinationItem = document.createElement('div');
            combinationItem.className = 'combination-item';
            combinationItem.setAttribute('data-combination-id', combo.id);

            // 조합 정보 영역 생성
            const combinationInfo = document.createElement('div');
            combinationInfo.className = 'combination-info';

            // 옵션 정보 표시
            const combinationOptions = document.createElement('div');
            combinationOptions.className = 'combination-options';
            const optionText = Object.keys(combo.options).map(key =>
                `${key}: ${combo.options[key].value}`
            ).join(', ');
            combinationOptions.textContent = optionText;

            // 가격 정보 표시
            const combinationPrice = document.createElement('div');
            combinationPrice.className = 'combination-price';
            combinationPrice.textContent = `₩${(combo.price * combo.quantity).toLocaleString()}`;

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
            decreaseBtn.addEventListener('click', () => changeCombinationQuantity(combo.id, -1));

            // 수량 입력 필드
            const quantityInput = document.createElement('input');
            quantityInput.type = 'number';
            quantityInput.className = 'combination-quantity-input';
            quantityInput.value = combo.quantity;
            quantityInput.min = '1';
            quantityInput.addEventListener('change', (e) => setCombinationQuantity(combo.id, e.target.value));

            // 수량 증가 버튼
            const increaseBtn = document.createElement('button');
            increaseBtn.type = 'button';
            increaseBtn.className = 'combination-quantity-btn';
            increaseBtn.textContent = '+';
            increaseBtn.addEventListener('click', () => changeCombinationQuantity(combo.id, 1));

            // 수량 조절 영역에 버튼들 추가
            combinationQuantity.appendChild(decreaseBtn);
            combinationQuantity.appendChild(quantityInput);
            combinationQuantity.appendChild(increaseBtn);

            // 삭제 버튼 생성
            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.className = 'combination-remove';
            removeBtn.textContent = '삭제';
            removeBtn.addEventListener('click', () => removeCombination(combo.id));

            // 메인 컨테이너에 모든 요소 추가
            combinationItem.appendChild(combinationInfo);
            combinationItem.appendChild(combinationQuantity);
            combinationItem.appendChild(removeBtn);

            // 조합 리스트에 추가
            combinationList.appendChild(combinationItem);
        });
    }

    // 총 주문 금액 정보 업데이트
    updateTotalSummary();
};

/**
 * 특정 조합의 수량을 변경 (+1 또는 -1)
 * @param {number} combinationId - 조합 ID
 * @param {number} delta - 변경량 (+1 또는 -1)
 */
const changeCombinationQuantity = (combinationId, delta) => {
    const combination = selectedCombinations.find(combo => combo.id === combinationId);
    if (combination) {
        // 최소 수량은 1개로 제한
        combination.quantity = Math.max(1, combination.quantity + delta);
        updateCombinationDisplay();
    }
};

/**
 * 특정 조합의 수량을 직접 설정
 * @param {number} combinationId - 조합 ID
 * @param {string} newQuantity - 새로운 수량 (문자열)
 */
const setCombinationQuantity = (combinationId, newQuantity) => {
    const combination = selectedCombinations.find(combo => combo.id === combinationId);
    if (combination) {
        // 입력값을 정수로 변환하고 최소값 1 보장
        combination.quantity = Math.max(1, parseInt(newQuantity) || 1);
        updateCombinationDisplay();
    }
};

/**
 * 특정 조합을 목록에서 제거
 * @param {number} combinationId - 제거할 조합 ID
 */
const removeCombination = (combinationId) => {
    // 해당 ID를 제외한 조합들로 배열 재구성
    selectedCombinations = selectedCombinations.filter(combo => combo.id !== combinationId);
    updateCombinationDisplay();
};

/**
 * 전체 주문 정보 (총 수량, 총 가격) 계산 및 표시
 */
const updateTotalSummary = () => {
    // 모든 조합의 총 수량 계산
    const totalQuantity = selectedCombinations.reduce((sum, combo) => sum + combo.quantity, 0);

    // 모든 조합의 총 가격 계산
    const totalPrice = selectedCombinations.reduce((sum, combo) => sum + (combo.price * combo.quantity), 0);

    // DOM 요소 참조
    const totalQuantityElement = document.getElementById('totalQuantity');
    const totalPriceElement = document.getElementById('totalPrice');
    const totalSummary = document.getElementById('totalSummary');

    if (totalQuantity > 0) {
        // 계산된 값을 화면에 표시
        totalQuantityElement.textContent = `총 ${totalQuantity}개`;
        totalPriceElement.textContent = `₩${totalPrice.toLocaleString()}`;
        totalSummary.style.display = 'block'; // 요약 정보 표시
    } else {
        // 선택된 상품이 없으면 요약 정보 숨김
        totalSummary.style.display = 'none';
    }
};

// ====================================================
// 장바구니 및 구매 관련 함수들
// ====================================================

/**
 * 선택된 모든 조합을 장바구니에 담기
 * fetchHelper.post를 사용한 Ajax 요청
 */
const addAllToCart = async () => {
    // 선택된 조합이 없으면 경고 메시지
    if (selectedCombinations.length === 0) {
        alert('장바구니에 담을 상품이 없습니다. 먼저 옵션을 선택하고 조합을 추가해주세요.');
        return;
    }

    // 상품 정보를 전역 변수에서 가져오기
    const productId = window.productData?.id || 0;
    const productName = window.productData?.name || '';

    try {
        // 각 조합별로 개별 요청 처리
        for (const combo of selectedCombinations) {
            // FormData 생성 (RequestParam에 맞춤)
            const formData = new FormData();
            formData.append('productId', productId);
            formData.append('productName', productName);
            formData.append('optionIds', Object.values(combo.options).map(opt => opt.id).join(','));
            formData.append('optionNames', Object.keys(combo.options).map(key =>
                `${key}:${combo.options[key].value}`
            ).join('|'));
            formData.append('quantity', combo.quantity);
            formData.append('price', combo.price);

            // Ajax 요청 직전 상태의 데이터 로깅
            console.log('=== 장바구니 담기 요청 데이터 ===');
            console.log('요청 URL: /api/cart/add');
            console.log('요청 방식: POST (FormData)');
            console.log('상품 ID:', productId);
            console.log('상품명:', productName);
            console.log('옵션 ID들:', Object.values(combo.options).map(opt => opt.id).join(','));
            console.log('옵션명들:', Object.keys(combo.options).map(key => `${key}:${combo.options[key].value}`).join('|'));
            console.log('수량:', combo.quantity);
            console.log('단가:', combo.price);
            console.log('================================');

            // fetch를 사용한 FormData 전송 (fetchHelper는 JSON용이므로 직접 fetch 사용)
            const response = await fetch('/api/cart/add', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const result = await response.json();

            if (!result.success) {
                throw new Error(result.message);
            }

            console.log(`조합 ${combo.id} 장바구니 담기 성공:`, result);
        }

        // 모든 조합 처리 완료
        console.log('모든 조합 장바구니 담기 성공');
        alert(`${selectedCombinations.length}개의 조합이 장바구니에 담겼습니다.`);

        // 선택된 조합 목록 초기화 (선택사항)
        // selectedCombinations = [];
        // updateCombinationDisplay();

    } catch (error) {
        // 에러 처리
        console.error('장바구니 담기 실패:', error);
        alert(`장바구니 담기에 실패했습니다: ${error.message}`);
    }
};

/**
 * 선택된 조합으로 바로구매 처리
 */
const buyNow = () => {
    // 선택된 조합이 없으면 경고 메시지
    if (selectedCombinations.length === 0) {
        alert('구매할 상품이 없습니다. 먼저 옵션을 선택하고 조합을 추가해주세요.');
        return;
    }

    // 상품 정보를 전역 변수에서 가져오기
    const productId = window.productData?.id || 0;
    const productName = window.productData?.name || '';

    // 바로구매를 위한 데이터 준비
    const orderData = {
        productId: productId,
        productName: productName,
        combinations: selectedCombinations.map(combo => ({
            optionId: Object.values(combo.options).map(opt => opt.id).join(','),
            optionName: Object.keys(combo.options).map(key =>
                `${key}:${combo.options[key].value}`
            ).join('|'),
            quantity: combo.quantity,
            price: combo.price,
            totalPrice: combo.price * combo.quantity
        })),
        totalQuantity: selectedCombinations.reduce((sum, combo) => sum + combo.quantity, 0),
        totalAmount: selectedCombinations.reduce((sum, combo) => sum + (combo.price * combo.quantity), 0)
    };

    // 바로구매 데이터 로깅
    console.log('=== 바로구매 요청 데이터 ===');
    console.log('요청 URL: /order/direct');
    console.log('요청 방식: POST');
    console.log('요청 데이터:', JSON.stringify(orderData, null, 2));
    console.log('========================');

    // TODO: 주문 페이지로 이동 또는 Ajax 요청
    // window.location.href = '/order/direct?data=' + encodeURIComponent(JSON.stringify(orderData));

    // 임시 메시지 (실제 구현 전까지)
    alert('주문 페이지로 이동합니다.');
};





// ====================================================
// 초기화 및 이벤트 리스너
// ====================================================

/**
 * 페이지 로드 완료 시 초기화 작업
 */
const initializeProductDetail = (productData) => {
    // 전역 변수에 상품 데이터 저장
    window.productData = productData;

    // 상품 가격 정보 설정
    productPrice = productData.price;
    productDiscount = productData.discount;
    finalPrice = productDiscount > 0 ?
        productPrice - (productPrice * productDiscount / 100) :
        productPrice;

    // 조합 추가 버튼 초기 상태 설정
    updateAddCombinationButton();

    // 페이지 로드 시 상품 정보 로깅
    console.log('=== 상품 상세 페이지 로드 완료 ===');
    console.log('상품 ID:', productData.id);
    console.log('상품명:', productData.name);
    console.log('원가격:', productPrice.toLocaleString() + '원');
    console.log('할인율:', productDiscount + '%');
    console.log('판매가격:', finalPrice.toLocaleString() + '원');
    console.log('================================');
};