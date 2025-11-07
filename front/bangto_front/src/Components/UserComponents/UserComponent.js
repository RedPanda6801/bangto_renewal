import axios from "axios";
import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import "../UserComponent.css";
import UserInfoComponent from "./UserInfoComponent";
import UserPayListComponent from "./UserPaylistComponent";
import UserQNAInfoComponent from "./UserQNAInfoComponent";

Modal.setAppElement("#root");

const UserComponent = () => {
  const [selectedMenu, setSelectedMenu] = useState("주문 내역");
  const [isUserInfoOpen, setIsUserInfoOpen] = useState(false);

  const handleUserInfo = () => {
    setIsUserInfoOpen(true);
  };

  const renderContentForMenu = (menu) => {
    if ("내 정보 조회" == menu) {
      return <UserInfoComponent></UserInfoComponent>;
    } else if ("주문 내역" == menu) {
      return <UserPayListComponent></UserPayListComponent>;
    }else if ("내 QNA" == menu) {
      return <UserQNAInfoComponent></UserQNAInfoComponent>;
    } else {
      return <div>선택 메뉴 없음</div>;
    }
  };

  return (
    <>
      <div className="layout_User">
        <div className="box_User_Menu">
          <button
            className="btn_User_Menu"
            onClick={() => {
              handleUserInfo();
              setSelectedMenu("내 정보 조회");
            }}
          >
            내 정보 조회
          </button>
          <button
            className="btn_User_Menu"
            onClick={() => setSelectedMenu("주문 내역")}
          >
            주문 내역
          </button>
          <button
            className="btn_User_Menu"
            onClick={() => setSelectedMenu("내 QNA")}
          >
            내 QNA
          </button>
        </div>
        <div className="layout_User_Contents">
          {renderContentForMenu(selectedMenu)}
        </div>
      </div>
    </>
  );
};

export default UserComponent;
