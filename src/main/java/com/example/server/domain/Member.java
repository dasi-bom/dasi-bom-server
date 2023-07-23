	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name; // 사용자명
	private String username; // 아이디
	private String password;
	private String email;
	private String mobile;
	@Enumerated(EnumType.STRING)
	private RoleType role;
	@Enumerated(EnumType.STRING)
	private OAuth2Provider provider;
	private String providerId;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	private Image profileImage;
	private String nickname;
	@CreationTimestamp
	private Timestamp createDate;

	public void updateProfileInfo(String nickname) {
		if (StringUtils.isNotBlank(nickname)) {
			this.nickname = nickname;
		}
	}

	public void updateProfileImage(Image image) {
		this.profileImage = image;
	}
}
