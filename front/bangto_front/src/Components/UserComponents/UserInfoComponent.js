import axios from "axios";
import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import { useNavigate } from "react-router-dom";
import "../UserComponent.css";
import { resContent } from "../UtilComponent/ResponseData";
Modal.setAppElement("#root");

const UserInfoComponent = () => {
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [mainAddr, setMainAddr] = useState("");
  const [detailAddr, setDetailAddr] = useState("");
  const [zipNum, setZipNum] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/user/get-info`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })
      .then((res) => {
        const user = resContent(res);
        setEmail(user.email);
        setName(user.name);
        setPhone(user.phone);
        if (user.addr !== "undefined!!!!undefined" && user.addr !== null) {
          const addrList = user.addr.split("!!");
          setMainAddr(addrList[0]);
          setDetailAddr(addrList[1]);
          setZipNum(addrList[2]);
        }
      })
      .catch((err) => {
        alert("로그인 오류");
        navigate(-1);
      });
  }, []);

  const handleUserDelete = async () => {
    if (window.confirm("정말 탈퇴하시겠습니까??")) {
      try {
        const response = await axios.post(
          `${process.env.REACT_APP_BACKEND_SERVER_PORT}/user/delete-me`,
          {},
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (response.status == 200) {
          localStorage.clear();
          alert("탈퇴 완료");
          navigate("/", { state: { category: "Main" } });
        }
      } catch (err) {
        alert("회원탈퇴 실패");
      }
    }
  };
  return (
    <div className="box_User_Info">
      <input
        className="user_Email"
        type="email"
        placeholder="* 이메일"
        value={email}
        readOnly
        onChange={(e) => setEmail(e.target.value)}
      />
      <input
        className="user_Name"
        type="text"
        placeholder="* 별명"
        value={name}
        readOnly
        onChange={(e) => setName(e.target.value)}
      />
      <input
        className="user_Tel"
        type="tel"
        placeholder="전화번호"
        value={phone ? phone : "입력된 전화번호가 없습니다."}
        //onChange={(e) => setPhone(e.target.value)}
        readOnly
      />
      <input
        className="user_Addr_Detail"
        type="text"
        value={mainAddr}
        //onChange={(e) => setDetailAddr(e.target.value)}
        readOnly
        placeholder="상세 주소"
      />
      <input
        className="user_Addr_Detail"
        type="text"
        value={detailAddr}
        //onChange={(e) => setDetailAddr(e.target.value)}
        readOnly
        placeholder="상세 주소"
      />
      <input
        className="user_Addr_Zip"
        type="text"
        value={zipNum}
        placeholder="우편번호"
        readOnly
      />
      <button className="btn_User_Info" onClick={handleUserDelete}>
        회원탈퇴
      </button>
    </div>
  );
};

export default UserInfoComponent;
