import React from "react";
import { useNavigate } from "react-router-dom";
import "../UserGroupItemComponent.css";
import { useEffect, useState } from "react";
import axios from "axios";
import { resContent } from "../UtilComponent/ResponseData";
import { CategoryType } from "../UtilComponent/DataFormat";
import UserGroupItemDetailComponent from "./UserGroupItemDetailComponent";

const UserGroupItemComponent = () => {
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [period, setPeriod] = useState({});
  const [leftTime, setLeftTime] = useState("");
  const [currentSlide, setCurrentSlide] = useState(0);
  const [groupItemList, setGroupItemList] = useState([]);
  const [selectedGroupItem, setSelectedGroupItem] = useState({});

  const moveToNextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % totalSlides);
  };
  const moveToPrevSlide = () => {
    setCurrentSlide((prev) => (prev - 1 + totalSlides) % totalSlides);
  };
  const slides = [
    "/images/03_main/group1.png",
    "/images/03_main/group2.png",
    "/images/03_main/group3.png",
  ];
  const totalSlides = slides.length;

  useEffect(() => {
    handleGetPeriod();
  }, []);

  useEffect(() => {
    if (!endDate || endDate == "") return; // endDate가 없으면 실행 안 함
    const interval = setInterval(() => {
      setLeftTime(handleLeftTime());
    }, 1000);

    return () => clearInterval(interval); // 컴포넌트 언마운트 시 정리
  }, [endDate]);

  useEffect(() => {
    handleGroupItemList();
  }, [period]);

  const handleLeftTime = () => {
    const end = new Date(endDate); // 문자열을 Date 객체로 변환
    const now = new Date(); // 현재 시간
    const diff = Math.max(0, end.getTime() - now.getTime()); // 음수 방지

    const day = Math.floor(diff / (1000 * 60 * 60) / 24);
    const hour = Math.floor((diff / (1000 * 60 * 60)) % 24);
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((diff % (1000 * 60)) / 1000);

    return `${day}일 ${hour}시간 ${String(minutes).padStart(2, "0")}분 ${String(
      seconds
    ).padStart(2, "0")}초`;
  };

  const handleGetPeriod = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-buy/current-event`,
        {
          withCredentials: true,
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      );
      console.log(response);
      if (response.status == 200) {
        const curPeriod = resContent(response);
        setPeriod(curPeriod);
        setStartDate(curPeriod.startDate);
        setEndDate(curPeriod.endDate);
      }
    } catch (err) {
      console.log(err);
    }
  };

  const handleGroupItemList = async () => {
    axios
      .get(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-item/event/get-list/${period.id}`,
        {
          withCredentials: true,
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        }
      )
      .then((res) => {
        console.log(res);
        if (res.status == 200) {
          setGroupItemList(resContent(res));
        }
      })
      .catch((err) => console.log(err));
  };

  return (
    <div className="layout_Main_Contents">
      {selectedGroupItem && Object.keys(selectedGroupItem).length > 0 ? (
        <UserGroupItemDetailComponent
          groupItem={selectedGroupItem}
          setGroupItem={setSelectedGroupItem}
        ></UserGroupItemDetailComponent>
      ) : (
        <>
          <h1>방토샵 공동 구매</h1>
          <div className="box_Slide">
            <div className="box_item box_slider">
              <div
                className="slider_track"
                style={{ justifyContent: "center" }}
              >
                <img
                  src={slides[currentSlide]}
                  alt={`슬라이드 ${currentSlide + 1}`}
                  style={{ display: "block", width: "50%" }}
                />
                <button onClick={moveToPrevSlide} className="slider_arrow left">
                  &#10094;
                </button>
                <button
                  onClick={moveToNextSlide}
                  className="slider_arrow right"
                >
                  &#10095;
                </button>
              </div>
            </div>
          </div>
          <div className="box_Group_Guide">
            <p>공동 구매 이용 안내</p>
            <p>공동 구매는 기본적으로 대량 구매를 지향하기 위한 서비스입니다.<br></br>
              공동 구매 물건은 기본적으로 10% 할인된 가격에 구매 가능합니다.<br></br>
              단, 판매자가 설정한 1인 최소 개수를 초과할 시에 15% 추가 할인이 적용됩니다.
            </p>
          </div>
          <div>
            <br />
            <div className="text_Recommend">현재 공동 구매 물품 목록</div>
            <div>
            {leftTime ? (
              <div style={{ textAlign: "center" }}>
                <h3 style={{ color: "red", fontSize: "bold" }}>
                  {leftTime}까지!
                </h3>
                <h5 style={{fontWeight : "bold"}}>얼마 남지 않았어요!</h5>
                <br>
                </br>
              </div>
            ) : (
              <div>현재 공동 구매 기간이 아닙니다.</div>
            )}
          </div>
            <div className="group_item_list">
              {groupItemList && groupItemList.length > 0 ? (
                groupItemList.map((groupInfo) => (
                  <div
                    className="group_Item"
                    onClick={() => setSelectedGroupItem(groupInfo)}
                  >
                    <img
                      className="group_item_img"
                      src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${
                        groupInfo.item.img.split("/")[0]
                      }`}
                    ></img>
                    <div>물품 이름: {groupInfo.item.title}</div>
                    <div>물품 가격: {groupInfo.item.price}</div>
                    <div>
                      물품 카테고리: {CategoryType[groupInfo.item.category]}
                    </div>
                  </div>
                ))
              ) : (
                <div>현재 등록된 공동 구매 물품이 없습니다 ㅠ.ㅠ</div>
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default UserGroupItemComponent;
