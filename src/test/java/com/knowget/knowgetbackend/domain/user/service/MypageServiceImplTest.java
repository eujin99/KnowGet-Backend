package com.knowget.knowgetbackend.domain.user.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MypageServiceImplTest {
	// @Mock
	// private UserRepository userRepository;
	//
	// @Mock
	// private PasswordEncoder passwordEncoder;
	//
	// @Mock
	// private SuccessCaseRepository successCaseRepository;
	//
	// @Mock
	// private CounselingRepository counselingRepository;
	//
	// @Mock
	// private BookmarkRepository bookmarkRepository;
	//
	// @InjectMocks
	// private MypageServiceImpl mypageService;
	//
	// @BeforeEach
	// @DisplayName("Mock 설정")
	// void setUp() {
	// 	MockitoAnnotations.openMocks(this);
	// }
	//
	// @Test
	// @WithMockUser(username = "testUser")
	// @DisplayName("getBookmarkList 성공 케이스")
	// void getBookmarkList_success() {
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.build();
	//
	// 	Bookmark bookmark1 = Bookmark.builder().user(user).build();
	// 	Bookmark bookmark2 = Bookmark.builder().user(user).build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	// 	when(bookmarkRepository.findByUser(any(User.class))).thenReturn(Arrays.asList(bookmark1, bookmark2));
	//
	// 	List<PostResponseDTO> bookmarks = mypageService.getBookmarkList();
	//
	// 	assertNotNull(bookmarks);
	// 	assertEquals(2, bookmarks.size());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(bookmarkRepository, times(1)).findByUser(any(User.class));
	// }
	//
	// @Test
	// @DisplayName("updatePrefLocation 성공 케이스")
	// void updatePrefLocation_success() {
	// 	LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO("testUser", "newLocation");
	//
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	//
	// 	String result = mypageService.updatePrefLocation(locationUpdateDTO);
	//
	// 	assertEquals("근무 희망 지역이 변경되었습니다", result);
	// 	assertEquals("newLocation", user.getPrefLocation());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(userRepository, times(1)).save(user);
	// }
	//
	// @Test
	// @DisplayName("updatePrefLocation 실패 케이스: 사용자 찾을 수 없음")
	// void updatePrefLocation_userNotFound() {
	// 	LocationUpdateDTO locationUpdateDTO = new LocationUpdateDTO("testUser", "newLocation");
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
	//
	// 	UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	// 		mypageService.updatePrefLocation(locationUpdateDTO);
	// 	});
	//
	// 	assertEquals("존재하지 않는 사용자입니다", exception.getMessage());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(userRepository, times(0)).save(any(User.class));
	// }
	//
	// @Test
	// @DisplayName("updatePrefJob 성공 케이스")
	// void updatePrefJob_success() {
	// 	JobUpdateDTO jobUpdateDTO = new JobUpdateDTO("newJob", "testUser");
	//
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	//
	// 	String result = mypageService.updatePrefJob(jobUpdateDTO);
	//
	// 	assertEquals("근무 희망 직종이 변경되었습니다", result);
	// 	assertEquals("newJob", user.getPrefJob());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(userRepository, times(1)).save(user);
	// }
	//
	// @Test
	// @DisplayName("updatePrefJob 실패 케이스: 사용자 찾을 수 없음")
	// void updatePrefJob_userNotFound() {
	// 	JobUpdateDTO jobUpdateDTO = new JobUpdateDTO("newJob", "testUser");
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
	//
	// 	UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	// 		mypageService.updatePrefJob(jobUpdateDTO);
	// 	});
	//
	// 	assertEquals("존재하지 않는 사용자입니다", exception.getMessage());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(userRepository, times(0)).save(any(User.class));
	// }
	//
	// @Test
	// @DisplayName("updatePassword 성공 케이스")
	// void updatePassword_success() {
	// 	PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("testUser", "newPassword");
	//
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	// 	when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
	//
	// 	String result = mypageService.updatePassword(passwordUpdateDTO);
	//
	// 	assertEquals("비밀번호가 변경되었습니다", result);
	// 	assertEquals("encodedPassword", user.getPassword());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(passwordEncoder, times(1)).encode(anyString());
	// 	verify(userRepository, times(1)).save(user);
	// }
	//
	// @Test
	// @DisplayName("updatePassword 실패 케이스: 비밀번호 입력 안됨")
	// void updatePassword_noPassword() {
	// 	PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("testUser", null);
	//
	// 	IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	// 		mypageService.updatePassword(passwordUpdateDTO);
	// 	});
	//
	// 	assertEquals("비밀번호가 입력되지 않았습니다", exception.getMessage());
	// 	verify(userRepository, times(0)).findByUsername(anyString());
	// 	verify(passwordEncoder, times(0)).encode(anyString());
	// 	verify(userRepository, times(0)).save(any(User.class));
	// }
	//
	// @Test
	// @DisplayName("updatePassword 실패 케이스: 사용자 찾을 수 없음")
	// void updatePassword_userNotFound() {
	// 	PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO("testUser", "newPassword");
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
	//
	// 	UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	// 		mypageService.updatePassword(passwordUpdateDTO);
	// 	});
	//
	// 	assertEquals("존재하지 않는 사용자입니다", exception.getMessage());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(passwordEncoder, times(0)).encode(anyString());
	// 	verify(userRepository, times(0)).save(any(User.class));
	// }
	//
	// @Test
	// @WithMockUser(username = "testUser")
	// @DisplayName("getCounselingList 성공 케이스")
	// void getCounselingList_success() {
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.build();
	//
	// 	Counseling counseling1 = Counseling.builder().user(user).build();
	// 	Counseling counseling2 = Counseling.builder().user(user).build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	// 	when(counselingRepository.findAllByUserOrderBySentDate(any(User.class))).thenReturn(
	// 		Arrays.asList(counseling1, counseling2));
	//
	// 	List<CounselingResponseDTO> counselingList = mypageService.getCounselingList();
	//
	// 	assertNotNull(counselingList);
	// 	assertEquals(2, counselingList.size());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(counselingRepository, times(1)).findAllByUserOrderBySentDate(any(User.class));
	// }
	//
	// @Test
	// @WithMockUser(username = "testUser")
	// @DisplayName("getAllSuccessList 성공 케이스")
	// void getAllSuccessList_success() {
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.build();
	// 	SuccessCase successCase1 = SuccessCase.builder()
	// 		.title("title1")
	// 		.content("content1")
	// 		.isApproved(1)
	// 		.user(user)
	// 		.build();
	// 	SuccessCase successCase2 = SuccessCase.builder()
	// 		.title("title2")
	// 		.content("content2")
	// 		.isApproved(1)
	// 		.user(user)
	// 		.build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	// 	when(successCaseRepository.findAllByUserOrderByCreatedDateDesc(any(User.class))).thenReturn(
	// 		Arrays.asList(successCase1, successCase2));
	//
	// 	List<WrittenSuccessCaseDTO> successList = mypageService.getAllSuccessList();
	//
	// 	assertNotNull(successList);
	// 	assertEquals(2, successList.size());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(successCaseRepository, times(1)).findAllByUserOrderByCreatedDateDesc(any(User.class));
	// }
	//
	// @Test
	// @DisplayName("getUserInfo 성공 케이스")
	// void getUserInfo_success() {
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	//
	// 	UserInfoDTO userInfo = mypageService.getUserInfo("testUser");
	//
	// 	assertNotNull(userInfo);
	// 	assertEquals("testUser", userInfo.getUsername());
	// 	assertEquals("location", userInfo.getPrefLocation());
	// 	assertEquals("job", userInfo.getPrefJob());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// }
	//
	// @Test
	// @DisplayName("getUserInfo 실패 케이스: 사용자 찾을 수 없음")
	// void getUserInfo_userNotFound() {
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
	//
	// 	UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	// 		mypageService.getUserInfo("testUser");
	// 	});
	//
	// 	assertEquals("존재하지 않는 사용자입니다", exception.getMessage());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// }
	//
	// @Test
	// @DisplayName("deactivateUser 성공 케이스")
	// void deactivateUser_success() {
	// 	User user = User.builder()
	// 		.username("testUser")
	// 		.password("password")
	// 		.prefLocation("location")
	// 		.prefJob("job")
	// 		.role("USER")
	// 		.isActive(true)
	// 		.build();
	//
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
	//
	// 	String result = mypageService.deactivateUser("testUser");
	//
	// 	assertEquals("사용자(testUser)가 비활성화되었습니다", result);
	// 	assertFalse(user.getIsActive());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(userRepository, times(1)).save(user);
	// }
	//
	// @Test
	// @DisplayName("deactivateUser 실패 케이스: 사용자 찾을 수 없음")
	// void deactivateUser_userNotFound() {
	// 	when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
	//
	// 	UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	// 		mypageService.deactivateUser("testUser");
	// 	});
	//
	// 	assertEquals("존재하지 않는 사용자입니다", exception.getMessage());
	// 	verify(userRepository, times(1)).findByUsername(anyString());
	// 	verify(userRepository, times(0)).save(any(User.class));
	// }
}