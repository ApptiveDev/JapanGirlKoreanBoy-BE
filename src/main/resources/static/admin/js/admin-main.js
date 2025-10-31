// 전역 변수
let currentGender = 'FEMALE';
let currentPage = 1;
let currentFilters = {
    status: '',
    dateSort: 'desc',
    search: ''
};

// DOM 로드 완료 후 실행
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    loadMembers();
});

// 이벤트 리스너 초기화
function initializeEventListeners() {
    // 탭 전환
    const navTabs = document.querySelectorAll('.nav-tab');
    navTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const gender = this.dataset.gender;
            switchTab(gender);
        });
    });

    // 필터 변경
    document.getElementById('status-filter').addEventListener('change', function() {
        currentFilters.status = this.value;
        currentPage = 1;
        loadMembers();
    });

    document.getElementById('date-sort').addEventListener('change', function() {
        currentFilters.dateSort = this.value;
        currentPage = 1;
        loadMembers();
    });

    // 검색
    document.getElementById('search-btn').addEventListener('click', function() {
        currentFilters.search = document.getElementById('search-input').value;
        currentPage = 1;
        loadMembers();
    });

    document.getElementById('search-input').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            currentFilters.search = this.value;
            currentPage = 1;
            loadMembers();
        }
    });

    // 필터 초기화
    document.getElementById('reset-filter').addEventListener('click', function() {
        resetFilters();
    });

    // 페이지네이션
    document.getElementById('prev-page').addEventListener('click', function() {
        if (currentPage > 1) {
            currentPage--;
            loadMembers();
        }
    });

    document.getElementById('next-page').addEventListener('click', function() {
        currentPage++;
        loadMembers();
    });

    // 모달 관련
    document.querySelector('.modal-close').addEventListener('click', closeModal);
    document.getElementById('modal-cancel').addEventListener('click', closeModal);
    document.getElementById('modal-confirm').addEventListener('click', confirmStatusChange);

    // 모달 외부 클릭 시 닫기
    document.getElementById('status-modal').addEventListener('click', function(e) {
        if (e.target === this) {
            closeModal();
        }
    });
}

// 탭 전환
function switchTab(gender) {
    currentGender = gender;
    currentPage = 1;

    // 탭 활성화 상태 변경
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.classList.remove('active');
        if (tab.dataset.gender === gender) {
            tab.classList.add('active');
        }
    });

    // 타이틀 변경
    const title = gender === 'FEMALE' ? '일본 여자' : '한국 남자';
    document.getElementById('current-tab-title').textContent = `${title} 회원 관리`;

    // 필터 초기화
    resetFilters();

    // 데이터 로드
    loadMembers();
}

// 필터 초기화
function resetFilters() {
    currentFilters = {
        status: '',
        dateSort: 'desc',
        search: ''
    };

    document.getElementById('status-filter').value = '';
    document.getElementById('date-sort').value = 'desc';
    document.getElementById('search-input').value = '';

    currentPage = 1;
    loadMembers();
}

// 회원 데이터 로드 (실제 API 호출 부분)
function loadMembers() {
    // TODO: 실제 API 호출로 대체
    // 예시: fetch(`/api/admin/members?gender=${currentGender}&status=${currentFilters.status}...`)

    // 임시 더미 데이터
    const dummyData = generateDummyData();
    renderMemberTable(dummyData);
    updatePagination(dummyData.totalPages);
    updateTotalCount(dummyData.totalCount);
}

// 더미 데이터 생성 (테스트용)
function generateDummyData() {
    const statuses = ['PENDING_APPROVAL', 'APPROVED', 'CONNECTING', 'CONNECTED', 'BLACKLISTED'];
    const names = currentGender === 'FEMALE'
        ? ['사쿠라', '유키', '아야', '미사키', '하루카']
        : ['민수', '지훈', '현우', '태양', '준호'];

    const members = [];
    for (let i = 1; i <= 10; i++) {
        members.push({
            id: i,
            name: names[Math.floor(Math.random() * names.length)],
            email: `user${i}@example.com`,
            status: statuses[Math.floor(Math.random() * statuses.length)],
            createdAt: new Date(2024, Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1),
            residenceArea: currentGender === 'FEMALE' ? '도쿄' : '서울'
        });
    }

    return {
        members: members,
        totalPages: 5,
        totalCount: 47
    };
}

// 테이블 렌더링
function renderMemberTable(data) {
    const tbody = document.getElementById('member-table-body');

    if (data.members.length === 0) {
        tbody.innerHTML = `
            <tr class="empty-row">
                <td colspan="7">
                    <div class="empty-state">
                        <p>조회된 회원이 없습니다.</p>
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = data.members.map(member => `
        <tr>
            <td>${member.id}</td>
            <td>${member.name}</td>
            <td>${member.email}</td>
            <td>
                <span class="status-badge ${getStatusClass(member.status)}">
                    ${getStatusText(member.status)}
                </span>
            </td>
            <td>${formatDate(member.createdAt)}</td>
            <td>${member.residenceArea}</td>
            <td>
                <button class="action-btn" onclick="openStatusModal(${member.id}, '${member.name}', '${member.status}')">
                    상태 변경
                </button>
            </td>
        </tr>
    `).join('');
}

// 상태 CSS 클래스 반환
function getStatusClass(status) {
    const statusMap = {
        'PENDING_APPROVAL': 'status-pending',
        'APPROVED': 'status-approved',
        'CONNECTING': 'status-connecting',
        'CONNECTED': 'status-connected',
        'BLACKLISTED': 'status-blacklisted'
    };
    return statusMap[status] || '';
}

// 상태 텍스트 반환
function getStatusText(status) {
    const statusMap = {
        'PENDING_APPROVAL': '승인대기',
        'APPROVED': '승인완료',
        'CONNECTING': '연결중',
        'CONNECTED': '연결됨',
        'BLACKLISTED': '블랙 유저'
    };
    return statusMap[status] || status;
}

// 날짜 포맷팅
function formatDate(date) {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 페이지네이션 업데이트
function updatePagination(totalPages) {
    const pageNumbers = document.getElementById('page-numbers');
    const prevBtn = document.getElementById('prev-page');
    const nextBtn = document.getElementById('next-page');

    // 이전/다음 버튼 상태
    prevBtn.disabled = currentPage === 1;
    nextBtn.disabled = currentPage === totalPages;

    // 페이지 번호 생성
    let pageHTML = '';
    const maxPages = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxPages / 2));
    let endPage = Math.min(totalPages, startPage + maxPages - 1);

    if (endPage - startPage < maxPages - 1) {
        startPage = Math.max(1, endPage - maxPages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        pageHTML += `
            <button class="page-number ${i === currentPage ? 'active' : ''}" 
                    onclick="goToPage(${i})">
                ${i}
            </button>
        `;
    }

    pageNumbers.innerHTML = pageHTML;
}

// 페이지 이동
function goToPage(page) {
    currentPage = page;
    loadMembers();
}

// 전체 회원 수 업데이트
function updateTotalCount(count) {
    document.getElementById('total-count').textContent = count;
}

// 상태 변경 모달 열기
let selectedMemberId = null;

function openStatusModal(memberId, memberName, currentStatus) {
    selectedMemberId = memberId;
    document.getElementById('modal-member-name').textContent = memberName;
    document.getElementById('new-status').value = currentStatus;
    document.getElementById('status-modal').classList.add('show');
}

// 모달 닫기
function closeModal() {
    document.getElementById('status-modal').classList.remove('show');
    selectedMemberId = null;
}

// 상태 변경 확인
function confirmStatusChange() {
    const newStatus = document.getElementById('new-status').value;

    if (!selectedMemberId) return;

    // TODO: 실제 API 호출로 대체
    console.log(`회원 ${selectedMemberId}의 상태를 ${newStatus}로 변경`);

    // 임시: 알림 표시
    alert('상태가 변경되었습니다.');

    closeModal();
    loadMembers();
}
