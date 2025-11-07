import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../StoreModiComponent.css";
import Modal from "react-modal";
Modal.setAppElement("#root");

const StoreAddComponent = (props) => {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [busiNum, setBusiNum] = useState("");

  let isStoreValid = name;

  useEffect(() => {
    console.log(props.store);
  }, [props.modal]);

  // 입력 항목 유효성 검사
  const handleStoreAuth = async () => {
    try {
      console.log(props.store);
      const validChar = /^[a-zA-Z가-힣0-9]+$/;

      if (0 < name.length && 11 > name.length && validChar.test(name))
        isStoreValid = true;

      const response = await axios.post(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/store/add`,
        {
          busiNum,
          name,
        },
        {
          withCredentials: true,
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      );

      if (!isStoreValid) {
        alert(
          "매장 이름 입력이 잘못되었습니다. (1 ~ 10자로 입력 가능, 특수문자 입력 불가)"
        );
      } else {
        if (response.status == 200) {
          alert("매장 추가가 완료되었습니다.");
          navigate(0);
        } else {
          console.error("매장 추가 실패", response.data);
        }
      }
    } catch (error) {
      alert("매장 추가가 실패하였습니다.");
      console.error("매장 추가 실패", error);
      if (error.response) {
        console.error("매장 추가 실패", error.response.data);
      }
    }
  };

  const handleCloseModal = () => {
    props.setModal(false);
  };

  return (
    <Modal
      isOpen={props.modal}
      onRequestClose={handleCloseModal}
      className="modal_Manager_Store_Modi"
      overlayClassName="modal_Manager_User_Modi_Overlay"
    >
      <div className="layout_Store_Modi">
        <div className="box_Store_Modi">
          <input
            className="Modi_Seller_Name"
            type="text"
            placeholder={props.sellerName}
            defaultValue={props.sellerName}
            disabled
          />
          <input
            className="Modi_Store_Name"
            type="text"
            placeholder="매장 이름"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            className="Modi_Busi_Num"
            type="text"
            placeholder="사업자 번호"
            value={busiNum}
            onChange={(e) => setBusiNum(e.target.value)}
          />
          <div className="box_btn_Store_Modi">
            <input
              className="btn_Store_Modi"
              type="button"
              value="추가"
              disabled={!isStoreValid}
              onClick={handleStoreAuth}
            />
            <input
              className="btn_Store_Modi"
              type="button"
              value="취소"
              onClick={() => handleCloseModal()}
            />
          </div>
          {!name && (
            <p className="StoreModi_Ck_Error_Message">
              * 매장 이름을 입력해주세요
            </p>
          )}
        </div>
      </div>
    </Modal>
  );
};

export default StoreAddComponent;
