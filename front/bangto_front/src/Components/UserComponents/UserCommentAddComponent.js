import { useEffect, useState } from "react";
import Modal from "react-modal";
import axios from "axios";
import "../UserReviewComponent.css";
Modal.setAppElement("#root");

const UserCommentAddComponent = (props) => {
  const [rating, setRating] = useState(0);
  const [content, setContent] = useState("");
  const [images, setImages] = useState([]);
  const [files, setFiles] = useState("");

  const handleAddComment = async () => {
    console.log(rating, content);
    try {
      const dto = {
        soldItemPk: props.id,
        star: rating,
        content,
      };
      const formData = new FormData();
      formData.append(
        "dto",
        new Blob([JSON.stringify(dto)], { type: "application/json" })
      );
      files.forEach((file) => {
        formData.append("files", file);
      });
      const response = await axios.post(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/comment/write`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      if (response.status == 200) {
        alert("후기 등록 완료");
        props.setModal(false);
      }
    } catch (err) {
      if(err.response.data == "배송 완료된 물품만 후기를 작성할 수 있습니다."){
        alert("배송 완료된 물품만 후기를 작성할 수 있습니다.");
      }else{
        alert("후기 등록 실패");
      }
    }
  };

  const handleStarClick = (index) => {
    setRating(index + 1);
  };

  const handleImageChange = (e) => {
    if (e.target.files.length > 0) {
      const filesArray = Array.from(e.target.files);
      setFiles(filesArray);
      filesArray.forEach((file) => {
        const reader = new FileReader();
        reader.onloadend = () => {
          setImages((prevImages) => [...prevImages, reader.result]);
        };
        reader.readAsDataURL(file);
      });
    }
  };

  return (
    <Modal
      isOpen={props.modal}
      onRequestClose={() => props.setModal(false)}
      className="modal_User_Review"
      overlayClassName="modal_User_Pay_Overlay"
    >
      <div className="layout_User_Review">
        <div className="box_User_Review">
          <table className="table_User_Review">
            <tr>
              <td>작성자</td>
              <td>{props.name} 님</td>
            </tr>
            <tr>
              <td>이미지 등록</td>
              <td>
                <div className="Item_Add_Img">
                  <input
                    type="file"
                    accept="image/*"
                    multiple
                    onChange={handleImageChange}
                    id="btn_Img_Upload"
                  />
                  <label htmlFor="btn_Img_Upload">
                    {images && images.length > 0 ? (
                      images.map((image) => (
                        <img src={image} alt="업로드된 이미지 미리보기" />
                      ))
                    ) : (
                      <div>이미지 등록</div>
                    )}
                  </label>
                </div>
              </td>
            </tr>
            <tr>
              <td>별점</td>
              <td>
                {Array.from({ length: 5 }, (_, index) => (
                  <span
                    key={index}
                    onClick={() => handleStarClick(index)}
                    style={{
                      cursor: "pointer",
                      color: index < rating ? "gold" : "gray",
                    }}
                  >
                    ★
                  </span>
                ))}
              </td>
            </tr>
            <tr>
              <td>내용</td>
              <td>
                <textarea
                  className="content_input_area"
                  onChange={(e) => setContent(e.target.value)}
                ></textarea>
              </td>
            </tr>
          </table>
          <div>
            <button className="btn_Review" onClick={() => handleAddComment()}>
              등록
            </button>
            <button
              className="btn_Review"
              onClick={() => props.setModal(false)}
            >
              취소
            </button>
          </div>
        </div>
      </div>
    </Modal>
  );
};

export default UserCommentAddComponent;
