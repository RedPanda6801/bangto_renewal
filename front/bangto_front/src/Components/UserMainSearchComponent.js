import { useState } from "react";
import { useNavigate } from "react-router-dom";

const UserMainSearchComponent = () => {
  const [searchText, setSearchText] = useState("");

  return <div className="box">
    <div className="box_Category">
        <input
            className="btn_Category"
            type="image"
            src={`/images/02_icon/icon_03.jpg`}
            alt="카테고리버튼">
        </input>
        <div className="list_Category">
            <button
                className="btn_Main_Category">
                의류
            </button>
            <button
                className="btn_Main_Category">
                화장품
            </button>
            <button
                className="btn_Main_Category">
                전자기기
            </button>
            <button
                className="btn_Main_Category">
                가구
            </button>
            <button
                className="btn_Main_Category">
                식품
            </button>
            <button
                className="btn_Main_Category">
                스포츠 용품
            </button>
            <button
                className="btn_Main_Category">
                유아 용품
            </button>
            <button
                className="btn_Main_Category">
                차량 용품
            </button>
            <button
                className="btn_Main_Category">
                반려동물 용품
            </button>
            <button
                className="btn_Main_Category">
                도서
            </button>
        </div>
    </div>
    <div className="box_Search">
        <div className="box_Search_Container">
            <input
                type="text"
                className="main_Search_Input"
                placeholder="검색어를 입력하세요"
                value={searchText}
                onChange={(e) => setSearchText(e.target.value)}/>
            <button 
                className="main_Search_Btn">
                검색
            </button>
        </div>
    </div>
  </div>;
}

export default UserMainSearchComponent;