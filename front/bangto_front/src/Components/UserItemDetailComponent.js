import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import './UserItemDetailComponent.css';
import UserCartListComponent from "./UserCartListComponent";
import axios from "axios";
import { resContent, resPage } from "./UtilComponent/ResponseData";
import { CategoryType, TimeStamp } from "./UtilComponent/DataFormat";
import PagenationComponent from "./UtilComponent/PagenationComponent";

const UserItemDetailComponent = () => 
{
    const navigate = useNavigate();
    const location = useLocation();
    const itemPk = location.state.itemPk;
    const [selectedMenu, setSelectedMenu] = useState("내용");
    const [item, setItem] = useState({});
    const [totalImage, setTotalImage] = useState(0);
    const [currentImage, setCurrentImage] = useState(0);
    const [isFavored, setIsFavored] = useState(false);
    const [isFavorite20, setIsFavorite20] = useState(false);
    const [amount, setAmount] = useState(0);
    const [selectedOption, setSelectedOption] = useState({});
    const [currentReviewPage, setCurrentReviewPage] = useState(1);
    const [totalReviewPage, setTotalReviewPage] = useState(1);
    const [reviews, setReviews] = useState([]);
    const [totalReivewImages, setTotalReviewImages] = useState([]);
    const [currentReviewImages, setCurrentReviewImages] = useState([]);
    const [currentQnaPage, setCurrentQnaPage] = useState(1);
    const [totlaQnaPage, setTotalQnaPage] = useState(1);
    const [qnas, setQnas] = useState([]);
    const [qnaWrite, setQnaWrite] = useState("");

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/item/get-detail/${itemPk}`)
        .then((res) => {
            const rescontent = resContent(res);
            setItem(rescontent);
            setTotalImage(rescontent.img.split("/").length);
            setCurrentImage(0);
            setSelectedOption(rescontent.options[0]);
            // console.log(rescontent);
            axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/favorite/get-list/1`,
                {   
                    withCredentials: true,
                    headers: {"Authorization": `Bearer ${localStorage.getItem("token")}`}
                }
            ).then((res) => {
                const rescontent = resContent(res);
                let favored = rescontent.some(content => content.item.id === itemPk);
                setIsFavored(favored);
                setIsFavorite20(rescontent.length >= 20);
            }).catch((err) => {
                setIsFavored(null);
                setIsFavorite20(null);
            })
        }).catch((err) => {
        })
    }, [itemPk])

    useEffect(() => {
        axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/comment/item/${itemPk}/${currentReviewPage}`)
        .then((res) => {
            const rescontent = resContent(res);
            const respage = resPage(res);
            setReviews(rescontent);
            setCurrentReviewPage(currentReviewPage);
            setTotalReviewPage(respage.totalPages);
            // console.log(rescontent);
            setTotalReviewImages(Array.from({length: rescontent.length}, (_, idx) => rescontent[idx].img?.split("/").length));
            setCurrentReviewImages(Array.from({length: rescontent.length}, () => 0));

        }).catch((err) => {
            // console.log(err);
        })
    }, [currentReviewPage])

    useEffect(() => {
        getQna();
    }, [currentQnaPage])

    const getQna = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/qna/item/get-list/${itemPk}/${currentQnaPage}`)
            if(response.status === 200) {
                const rescontent = resContent(response);
                const respage = resPage(response);
                // console.log(response);
                setQnas(rescontent);
                setCurrentQnaPage(currentQnaPage);
                setTotalQnaPage(respage.totalPages);
            }
        }
        catch (err) {
            alert(err);
        }
    }

    const handleWishlist = () => {
        if(isFavorite20) {
            alert("찜은 최대 20개까지 할 수 있습니다.");
            return;
        }
        axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/favorite/add`, {
            id : itemPk
        }, { withCredentials : true,
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }
        }).then(() => {
            setIsFavored(true);
            alert("찜 목록에 추가되었습니다.");
        });

    }

    const handleUnwishlist = () => {
        axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/favorite/delete`, {
            id : itemPk
        }, { withCredentials : true,
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }
        }).then(() => {
            setIsFavored(false);
            alert("찜 목록에서 해제되었습니다.")
        });
    }

    const handleCart = () => {
        axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/cart/add`, {
            optionPk : selectedOption.id,
            amount
        }, { withCredentials : true,
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }
        }).then(() => {
            if(window.confirm("장바구니에 추가되었습니다. 장바구니로 이동하시겠습니까?")) {
                navigate("/user/cart");
            }
        })
        .catch((err) => {
            alert(err.response.data);
        });
    }

    const handleOption = (e) => {
        const selectedId = e.target.value;
        const selectedObj = item.options.find(option => option.id.toString() === selectedId);
        setSelectedOption(selectedObj);
        if(selectedObj.amount < amount) {
            setAmount(selectedObj.amount);
        }
    }

    const handleAmount = (e) => {
        setAmount(selectedOption.amount < e.target.value ? selectedOption.amount : (e.target.value < 0 ? 0 : e.target.value));
    }

    const handleQna = () => {
        if(window.confirm("해당 내용으로 문의하시겠습니까?")) {   
            axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/qna/add`, {
                itemPk,
                qcontent: qnaWrite
            }, { withCredentials : true,
                headers: {
                    "Authorization": `Bearer ${localStorage.getItem("token")}`,
                }
            }).then(() => {
                alert("문의에 성공하셨습니다.");
                setCurrentQnaPage(1);
                getQna();
            })
            .catch((err) => {
                alert(err.response.data);
                console.log(err);
            });
        }
    }
    
    const renderContentForMenu = (menu) => 
    {
        if("내용" == menu)
        {
            return <div className="box_Content">
                {item.content}
            </div>;
        }
        else if("후기" == menu)
        {
            return reviews.length === 0 ? <div className="box_No_Review">
                첫 후기 작성자가 되어보세요!
            </div> :
            <div className="box_Item_Review">
                {
                    reviews.map((review, idx) => {
                        return <table className="table_Item_Review" key={`table${idx}`}>
                            <tr>
                                <td className="box_Review_Info">
                                    <div className="review_Writer">
                                        {review.writer}
                                    </div>
                                    <div className="review_Writedate">
                                        {TimeStamp(review.writeDate)}
                                    </div>
                                </td>
                                {
                                    review.img === null ?
                                    <td className="box_Review_Image">
                                        <div className="review_Image">
                                            이미지 없음
                                        </div>
                                    </td> :
                                    <td className="box_Review_Image">
                                        <img className="review_Image" src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${review.img?.split("/")[currentReviewImages[idx]] || ""}`} alt="후기 이미지" />
                                        <button
                                            onClick={() => {
                                                let result = [...currentReviewImages];
                                                result[idx] -= 1
                                                
                                                setCurrentReviewImages(result);
                                            }}
                                            className="review_imglist_arrow review_arrow_left"
                                            style={currentReviewImages[idx] === 0 ? {display:"none"} : {}}>
                                            &#10094;
                                        </button>
                                        <button
                                            onClick={() => {
                                                let result = [...currentReviewImages];
                                                result[idx] += 1
                                                setCurrentReviewImages(result);
                                            }}
                                            className="review_imglist_arrow review_arrow_right"
                                            style={currentReviewImages[idx] === totalReivewImages[idx] - 1 ? {display:"none"} : {}}>
                                            &#10095;
                                        </button>
                                    </td>
                                }
                                <td className="box_Review_Content">
                                    {review.content}
                                </td>
                            </tr>
                        </table>;
                    })
                }
                <PagenationComponent page={currentReviewPage} setPage={setCurrentReviewPage} totalPage={totalReviewPage} />
            </div>;
            }
        else if("문의" == menu)
        {
            return <div className="box_Btn_Item_QNA">
                        <div className="box_Qna_Write">
                            <textarea
                                className="box_Qna_Write_Content"
                                value={localStorage.getItem("USERROLE") === "GUEST" ? "로그인 후 이용하실 수 있습니다." : qnaWrite}
                                onChange={(e) => setQnaWrite(e.target.value)}
                                disabled = {localStorage.getItem("USERROLE") === "GUEST"}
                            />
                            <button 
                                className="btn_Regi_QNA"
                                onClick={handleQna}>
                                문의하기
                            </button>
                        </div> 
                        {
                            qnas.map((qna, idx) => {
                                return <div className="box_Item_Qna" key={`table${idx}`}>
                                    <table className="table_Item_Review">
                                        <tr>
                                            <td className="box_Review_Info">
                                                <div className="review_Writer">
                                                    {qna.userName}
                                                </div>
                                                <div className="review_Writedate">
                                                    {TimeStamp(qna.qwriteDate)}
                                                </div>
                                            </td>
                                            <td className="box_Review_Content">
                                                {qna.qcontent}
                                            </td>
                                        </tr>
                                    </table>
                                    {
                                        !qna.acontent ? null :
                                        <div className="box_Qna">
                                            <img
                                                src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/02_icon/reply.png`}
                                                alt="답변 이미지"
                                                className="qna_Image"    
                                            />
                                            <table className="table_Item_Review" key={`table${idx}`}>
                                                <tr>
                                                    <td className="box_Review_Info">
                                                        <div className="review_Writedate">
                                                            {TimeStamp(qna.awriteDate)}
                                                        </div>
                                                    </td>
                                                    <td className="box_Review_Content">
                                                        {qna.acontent}
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    }
                                </div>;
                            })
                        }
                        <PagenationComponent page={currentQnaPage} setPage={setCurrentQnaPage} totalPage={totlaQnaPage} />
                    </div>;
        }
        else
        {
          return <div>선택 메뉴 없음</div>
        }
    };

    return (
            item === undefined ? null:
            <div className="layout_Item_Detail_Contents">
                <div className="box_Item_Detail">
                    <div className="box_Item_Category">
                        {CategoryType[item.category]}
                    </div>
                    <div className="box_Item_Infomation">
                        <div className="box_Item_Img">
                            <img className="item_Img" src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${item.img?.split("/")[currentImage] || ""}`} alt="물품 이미지" />
                            <button
                                onClick={() => setCurrentImage(currentImage - 1)}
                                className="imglist_arrow arrow_left"
                                style={currentImage === 0 ? {display:"none"} : {}}>
                                &#10094;
                            </button>
                            <button
                                onClick={() => setCurrentImage(currentImage + 1)}
                                className="imglist_arrow arrow_right"
                                style={currentImage === totalImage - 1 ? {display:"none"} : {}}>
                                &#10095;
                            </button>
                        </div>
                        <div className="box_Item_Infobox">
                            {
                                isFavored === null ? null :
                                isFavored ?
                                <div 
                                    className="box_Item_Unwishlist"
                                    onClick={handleUnwishlist}
                                >
                                    찜 해제
                                </div>:
                                <div 
                                    className="box_Item_Wishlist"
                                    onClick={handleWishlist}
                                >
                                    찜
                                </div>
                            }
                            <div className="box_Item_Store">
                                {item.store?.name}
                            </div>
                            <div className="box_Item_Name">
                                {item.title}
                            </div>
                            <div className="box_Item_Price">
                                가격 : {item.price}원
                            </div>
                            <div className="box_Item_Opt">
                                <select value={selectedOption?.id || ""} onChange={handleOption}>
                                    {
                                        item.options?.map((option) => {
                                            return <option key={option.id} value={option.id}>
                                                + {option.addPrice}원 | {option.optionInfo} | 재고 : {option.amount}개
                                            </option>
                                        })
                                    }
                                </select>
                            </div>
                            <div className="box_Item_Amount">
                                재고 수량 : {item.amount}개
                            </div>
                            <div className="box_Add_Cart">
                                <input type="number" onChange={handleAmount} value={amount} className="input_Amount"/>
                                <div className="text_Amount">
                                    개
                                </div>
                                <div
                                    className="box_Item_Cart"
                                    onClick={handleCart}
                                >
                                    장바구니에 담기
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="box_Item_Content">
                        <div className="box_Item_Menu">
                            <button
                                className="btn_Item_Menu"
                                onClick={() => setSelectedMenu("내용")}>
                                내용
                            </button>
                            <button
                                className="btn_Item_Menu btn_Item_Menu_Center"
                                onClick={() => setSelectedMenu("후기")}>
                                후기
                            </button>
                            <button
                                className="btn_Item_Menu"
                                onClick={() => setSelectedMenu("문의")}>
                                문의
                            </button>
                        </div>
                        <div className="box_Item_Contents">
                            {renderContentForMenu(selectedMenu)}
                        </div>
                    </div>
                </div>
            </div>
        
    );
}

export default UserItemDetailComponent;