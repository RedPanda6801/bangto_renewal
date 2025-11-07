import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import './UserMainComponent.css';
import UserMainRecommendComponent from "./UserMainRecommendComponent";
import UserCartListComponent from "./UserCartListComponent"
import axios from "axios";
import { resContent, resPage } from "./UtilComponent/ResponseData";
import { useLocation, useNavigate } from "react-router-dom";
import PagenationComponent from "./UtilComponent/PagenationComponent";

const UserMainComponent = forwardRef((props, ref) => 
{    
    const [selectedCategory, setSelectedCategory] = useState("Main");
    const [searchText, setSearchText] = useState("");
    const [priceSort, setPriceSort] = useState("");
    const [items, setItems] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPage, setTotalPage] = useState(1);
    const navigate = useNavigate();

    const handleSearch = () => {
        if(searchText === "") {
            alert("검색어를 입력해주세요.");
        }
        else {
            setSelectedCategory("Search");
            if(selectedCategory !== "Main") {
                itemSearch();
            }
        }
    }

    useImperativeHandle(ref, () => ({
        setCategory : (value) => {setSelectedCategory(value); ; setPriceSort(""); setSearchText("")},
    }));

    const itemSearch = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/item/get-filtered-list`, {
                params: {
                    page: currentPage,
                    size: 20,
                    priceSort: priceSort === "" ? null : priceSort,
                    category: selectedCategory === "" || selectedCategory === "Search" || selectedCategory === "Main" ? null : selectedCategory,
                    title: selectedCategory === "Search" ? searchText : null
                }
            })
            if(response.status === 200) {
                setItems(resContent(response));
                // console.log(selectedCategory);
                // console.log(searchText);
                // console.log(resContent(response));
                setTotalPage(resPage(response).totalPages);
            }
        }
        catch (err) {
            alert(err);
        }
    }

    useEffect(() => {
        if(selectedCategory !== "Main") {
            itemSearch();
        }
    }, [selectedCategory, currentPage, priceSort])

    return (
        <div className="layout_Main_Contents">
            <div className="box">
                <div className="box_Category">
                    <input
                        className="btn_Category"
                        type="image"
                        src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/02_icon/icon_03.jpg`}
                        alt="카테고리버튼">
                    </input>
                    <div className="list_Category">
                        <button
                            className="btn_Main_Category"
                            onClick={() => {
                                setSelectedCategory("Clothing");
                                setPriceSort("");
                            }}>
                            의류
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Cosmetics"); setPriceSort("");}}>
                            화장품
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Electronics"); setPriceSort("");}}>
                            전자기기
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Furnitures"); setPriceSort("");}}>
                            가구
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Foods"); setPriceSort("");}}>
                            식품
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Sports"); setPriceSort("");}}>
                            스포츠 용품
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Kids"); setPriceSort("");}}>
                            유아 용품
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Cars"); setPriceSort("");}}>
                            차량 용품
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Pets"); setPriceSort("");}}>
                            반려동물 용품
                        </button>
                        <button
                            className="btn_Main_Category"
                            onClick={() => {setSelectedCategory("Books"); setPriceSort("");}}>
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
                            onChange={(e) => setSearchText(e.target.value)}
                            onKeyDown={(e) => {if(e.key === "Enter") {handleSearch();}}}
                        />
                        <button 
                            className="main_Search_Btn"
                            onClick={handleSearch}>
                            검색
                        </button>
                    </div>
                </div>
            </div>
            {
                selectedCategory === "Main" || selectedCategory === undefined?
                <UserMainRecommendComponent /> :
                (items.length === 0 ? 
                    <div className="box_No_Result">
                        검색 결과가 없습니다.
                    </div>:
                    <div>
                        <div className="sort-container">
                            <button
                            className={`sort-button ${priceSort === "asc" ? "asc" : ""}`}
                            onClick={() => setPriceSort("asc")}
                            >
                            가격 오름차순 ↑
                            </button>
                            <button
                            className={`sort-button ${priceSort === "desc" ? "desc" : ""}`}
                            onClick={() => setPriceSort("desc")}
                            >
                            가격 내림차순 ↓
                            </button>
                        </div>
                        <div className="box_Itemlist">
                            {
                                items.map((item, idx) => {
                                    return <div className="table_Itemlist" key={`searchedItem${idx}`}>
                                        <img
                                            className="itemlist_Item_Image"
                                            onClick={() => navigate("/user/item", {state: {itemPk: item.id}})}
                                            src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${item.img.split("/")[0]}`}
                                            alt="물품 이미지"/>
                                        <div className="itemlist_Item_Title">{item.title}</div>
                                        <div className="itemlist Item_Price">{item.price}원</div>
                                    </div>;
                                })
                            }
                        </div>
                        <PagenationComponent page={currentPage} setPage={setCurrentPage} totalPage={totalPage} />
                    </div>
                )
            }
            <UserCartListComponent />
        </div>
    );
});

export default UserMainComponent;