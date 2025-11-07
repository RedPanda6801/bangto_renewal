import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import './UserPayComponent.css';
import { resContent } from './UtilComponent/ResponseData';
import axios from 'axios';

Modal.setAppElement('#root');

const UserPayComponent = () => 
{
    const navigate = useNavigate();
    const location = useLocation();
    const carts = location.state.carts;
    const [payModal, setPayModal] = useState(false);
    const [keyword, setKeyword] = useState("");
    const [currentPage, setCurrentPage] = useState(1);
    const [addressList, setAddressList] = useState([]);
    const [isAddressSearchOpen, setIsAddressSearchOpen] = useState(false);
    const [totalPage, setTotalPageNum] = useState(1);
    const [visiblePages, setPageNums] = useState([]);
    const [roadAddress, setRoadAddress] = useState("");
    const [selectedAddress, setSelectedAddress] = useState("");
    const [detailAddr, setDetailAddr] = useState("");
    const [zipNumber, setZipNumber] =  useState("");
    const [myCash, setMyCash] = useState(0);
    const [myCashBack, setMyCashBack] = useState(0);
    const [payCashBack, setPayCashBack] = useState(0);
    const price = carts.reduce((acc, cart) => cart.totalPrice * cart.amount + acc, 0);
    const delivery = price >= 50000 ? 0 : (price >= 30000 ? 2500 : 5000);
    const [totalPay, setTotalPay] = useState(price + delivery);
    const USERROLE = localStorage.getItem("USERROLE");

    const handlePayModal = () => 
    {
        setPayModal(true);
    }

    const handleClosePayModal = () => 
    {
        setPayModal(false);
    }

    const handlePay = () => {
        axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/pay`, {
            cartPks : Array.from(carts, (cart) => cart.cartPk),
            usingCashBack : payCashBack
        }, { withCredentials : true,
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }
        }).then(() => {
            alert("결제에 성공하셨습니다.");
            navigate("/user");
        }).catch((err) => {
            console.log(err.response);
            alert(err);
        })
    }

    const getAddr = (page = 1) => 
    {
        const apiUrl = `https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do?currentPage=${page}&countPerPage=10&keyword=${encodeURIComponent(keyword)}&confmKey=devU01TX0FVVEgyMDI1MDIwNDEzMzEwMDExNTQ0MzY=&resultType=json&callback=handleApiResponse`;

        window.handleApiResponse = (data) => 
        {
            console.log("API 응답 데이터:", data);
            if (data.results.common.errorCode !== "0") 
            {
                switch(data.results.common.errorCode)
                {
                case "E0005":
                    alert("주소가 입력되지 않았습니다.");
                    break;
                case "E0006":
                    alert("주소를 상세히 입력해주세요");
                    break;
                case "E0008":
                    alert("주소를 한글자 이상 입력해주세요");
                    break;
                case "E0009":
                    alert("주소는 숫자만 검색하지못합니다.");
                    break;
                case "E0010":
                    alert("주소가 너무 깁니다.(한글 40자, 영문, 숫자 80자 이하로 입력)");
                    break;
                case "E0011":
                    alert("주소에 긴 숫자가 포함되어있습니다.(숫자 10자 이하로 입력)");
                    break;
                case "E0012":
                    alert("주소는 특수문자 + 숫자만으로는 검색이 불가능합니다.");
                    break;
                case "E0013":
                    alert("주소는 특수문자(%, =, >, <, [, ])는 검색이 불가능합니다.");
                    break;
                case "E0015":
                    alert("주소 검색 범위를 초과했습니다.");
                    break;
                default:
                    alert("주소가 잘못 입력되었습니다.");
                    break;
                }
            } 
            else 
            {
                setAddressList(data.results.juso);
                setIsAddressSearchOpen(true);
                setTotalPageNum(Math.ceil(data.results.common.totalCount / 10));
                updatePageNums(page, Math.ceil(data.results.common.totalCount / 10));
            }
        };

        const script = document.createElement('script');
        script.src = apiUrl;
        document.body.appendChild(script);
    }

    // 주소창 페이지 바꾸기
    const handlePageChange = (page) => 
    {
        setCurrentPage(page);
        getAddr(page);
        updatePageNums(page, totalPage);
    };

    // 선택한 주소 저장
    const handleAddressSelect = (address) => 
    {
        setSelectedAddress(address);
        setIsAddressSearchOpen(false);
    };

    // 페이지 번호 설정
    const updatePageNums = (currentPage, totalPage) =>
    {
        const pages = [];
        const range = 10;
        let start = Math.max(currentPage - 5, 1);
        let end = Math.min(start + range - 1, totalPage);

        if (end - start < range) 
        {
        start = Math.max(end - range + 1, 1);
        }

        for (let i = start; i <= end; i++)
        {
        pages.push(i);
        }
        setPageNums(pages);
    };

    useEffect(() => {
        if(USERROLE === "GUEST" || USERROLE === "ADMIN") {
            alert("비정상적인 접근입니다.");
            navigate("/", { state: { category: "Main" } });
        } else {
            axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/user/get-info`, {
                headers: {
                  "Authorization": `Bearer ${localStorage.getItem("token")}`,
                }
            }).then((res) => {
                const addrcontent = resContent(res);
                const addr = addrcontent.addr;
                // console.log(addrcontent);
                if(addr && addr.split("!!")[0] !== "undefined") {
                    const addrSplit = addr.split("!!");
                    setRoadAddress(addrSplit[0]);
                    setDetailAddr(addrSplit[1]);
                    setZipNumber(addrSplit[2]);
                }
                axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/wallet/my/get-info`, {
                    headers: {
                      "Authorization": `Bearer ${localStorage.getItem("token")}`,
                    }
                }).then((res) => {
                    const walletcontent = resContent(res);
                    // console.log(walletcontent);
                    setMyCash(walletcontent.cash);
                    setMyCashBack(walletcontent.cashBack);
                }).catch((err) => {
                    alert(err);
                })
            }).catch((err) => {
                alert(err);
            })
        }
    }, [])
    
    return <><div className="layout_User_Pay">
            <div className="box_User_Pay"> 
                {
                    carts.map((cart, idx) => {
                        return <table className="box_User_Pay_Items" key={`cart${idx}`}>
                            <tr>
                                <td rowSpan={2}>
                                    <img
                                        src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${cart.item.img.split("/")[0]}`}
                                        className="btn_User_Pay_Img"
                                        alt="물품 이미지"/>
                                </td>
                                <td colSpan={2} className="box_Overflow_Hidden box_Fontweight_Bold">
                                    {cart.item.title}
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td className="box_Overflow_Hidden">
                                    {cart.option.optionInfo}
                                </td>
                                <td className="box_Overflow_Hidden">
                                    {cart.amount}개
                                </td>
                                <td className="box_Overflow_Hidden">
                                    {cart.totalPrice}원
                                </td>
                            </tr>
                        </table>;
                    })
                }
                <line className="div_line"></line>
                <div>
                    <input
                        className="User_Pay_Addr"
                        type="text" 
                        value={keyword} 
                        onChange={(e) => setKeyword(e.target.value)} 
                        placeholder="주소 입력"/>
                    <button
                        className="User_Pay_Addr_Search"
                        onClick={() => { setCurrentPage(1); getAddr(1)}}>
                        검색
                    </button>
                </div>
                <table>
                <tr>
                    <td>
                        <div 
                            className="User_Pay_Addr_Road"
                            contentEditable={false}
                            tabindex="0">
                            {roadAddress ? roadAddress : (selectedAddress ? selectedAddress.roadAddr : "도로명 주소")}
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input 
                            className="User_Pay_Addr_Detail"
                            type="text" 
                            value={detailAddr}
                            onChange={(e) => setDetailAddr(e.target.value)}
                            placeholder="상세 주소"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input 
                            className="User_Pay_Addr_Zip"
                            type="text"
                            placeholder="우편번호" 
                            value={zipNumber ? zipNumber : (selectedAddress ? selectedAddress.zipNo : "")} 
                            readOnly/>
                    </td>
                </tr>
                </table> 
                <line className="div_line"></line>     
                <table
                    className="User_Pay_Cash">
                    <tr>
                        <td>
                            보유 캐시
                        </td>
                        <td>
                            {myCash} 원
                        </td>
                    </tr>
                </table>
                <table
                    className="User_Pay_Cash">
                    <tr>
                        <td>
                            총 상품 가격
                        </td>
                        <td>
                            {price} 원
                        </td>
                    </tr>
                    <tr>
                        <td>
                            배송비
                        </td>
                        <td>
                            {delivery} 원
                        </td>
                    </tr>
                    <tr>
                        <td>
                            총 결제 금액
                        </td>
                        <td>
                            {totalPay} 원
                        </td>
                    </tr>
                </table>
                <div>
                    <button 
                        className="btn_User_Pay_Cash"
                        onClick={() => handlePayModal()}>
                        결제하기
                    </button>
                    <button 
                        className="btn_User_Pay_Cash"
                        onClick={() => navigate(-1)}>
                        뒤로가기
                    </button>
                </div>
            </div>
        </div>  
        <Modal 
          isOpen={isAddressSearchOpen} 
          onRequestClose={() => setIsAddressSearchOpen(false)}
          contentLabel="주소 선택 창"
          className="modal_Addr"
          overlayClassName="modal_Overlay_Addr"
          ariaHideApp={false}>
          <h3>주소 선택</h3>
          <button 
            className="modal_Addr_Close" 
            onClick={() => setIsAddressSearchOpen(false)}>
            X
          </button>
          <table>
            <tr>
              <th>도로명 주소</th>
              <th>우편번호</th>
            </tr>
            {addressList.map((address, index) => (
              <tr 
                key={index} 
                onClick={() => handleAddressSelect(address)}>
                <td>{address.roadAddr}</td>
                <td>{address.zipNo}</td>
              </tr>
            ))}
          </table>
          <div className="pagination_Addr">
            {currentPage > 1 && (
              <button onClick={() => handlePageChange(currentPage - 1)}>
                &lt;
              </button>
            )}
            {visiblePages.map((page) => (
              <button
                key={page}
                onClick={() => handlePageChange(page)}
                className={currentPage == page ? 'active' : ''}>
                {page}
              </button>
            ))}
            {currentPage < totalPage && (
              <button onClick={() => handlePageChange(currentPage + 1)}>
                &gt;
              </button>
            )}
          </div>
        </Modal>      
        <Modal
            isOpen={payModal}
            onRequestClose={handleClosePayModal}
            className="modal_User_Pay"
            overlayClassName="modal_User_Pay_Overlay">
            <div class="box_User_Pay_Info">
                <div class="flex_Space_Between">
                    <div>
                        현재 보유 캐시
                    </div>
                    <div>
                        {myCash} 원
                    </div>
                </div>
                <div className="flex_Space_Between border_Top_Grey">
                    <div>
                        결제 후 캐시
                    </div>
                    <div>
                        {myCash - totalPay} 원
                    </div>
                </div>
            </div>
            <div class="box_User_Pay_Buttons">
                <button 
                    className="btn_User_Pay"
                    onClick={() => handlePay()}>
                    결제완료
                </button>
                <button 
                    className="btn_User_Pay"
                    onClick={() => setPayModal(false)}>
                    결제취소
                </button>
            </div>
        </Modal>
    </>
}

export default UserPayComponent;