package socket.client;

import java.io.Serializable;

public class Material implements Serializable {
	private Long materialId;
	private String materialName;
	private Long pressId;
	private String pressName;
	private Long schoolStageId;
	private String schoolStageName;
	private Long gradeId;
	private String gradeName;
	private Long subjectId;
	private String subjectName;
	private Integer ord;


	public Long getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public Long getPressId() {
		return pressId;
	}

	public void setPressId(Long pressId) {
		this.pressId = pressId;
	}

	public String getPressName() {
		return pressName;
	}

	public void setPressName(String pressName) {
		this.pressName = pressName;
	}

	public Long getSchoolStageId() {
		return schoolStageId;
	}

	public void setSchoolStageId(Long schoolStageId) {
		this.schoolStageId = schoolStageId;
	}

	public String getSchoolStageName() {
		return schoolStageName;
	}

	public void setSchoolStageName(String schoolStageName) {
		this.schoolStageName = schoolStageName;
	}

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}
}
