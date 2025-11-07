import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import './UserCartComponent.css';
import axios from 'axios';
import { resContent } from './UtilComponent/ResponseData';

// Modal.setAppElement('#root');

const UserCartComponent = () => 
{
    const navigate = useNavigate();
    const [optionModal, setOptionModal] = useState(false);
    const [carts, setCarts] = useState([]);
    const [isChecked, setIsChecked] = useState([]);
    const [allChecked, setAllChecked] = useState(false);
    const [totalPrice, setTotalPrice] = useState(0);
    const [deliverPrice, setDeliverPrice] = useState(0);
    const USERROLE = localStorage.getItem("USERROLE");

    useEffect(() => {
      if(USERROLE === "GUEST" || USERROLE === "ADMIN") {
        alert("비정상적인 접근입니다.");
        navigate("/", { state: { category: "Main" } });
      } else {
        axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/cart/get-info`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }
        }).then((res) => {
            const rescontent = resContent(res);
            setCarts(rescontent);
            setIsChecked(Array.from(rescontent, () => false));
            setAllChecked(false);
            // console.log(rescontent);
        }).catch((err) => {
            alert(err);
        })
      }
    }, [])

    const handlePayModal = () => 
    {
        setOptionModal(true);
    }

    const handleCloseOptionModal = () => 
    {
        setOptionModal(false);
    }

    const handleDelete = (cartPk) => {
      if(window.confirm("장바구니를 삭제하시겠습니까?")) {
        axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/cart/delete`, {
          cartPk
        }, { withCredentials : true,
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }
        });
        navigate(0);
      }
    }

    return <>
        <div className="layout_User_Cart">
            
                {
                  carts === null || carts.length === 0 ?
                  <div className="box_No_Item">
                      장바구니가 비었습니다.
                  </div>:
                  <div className="box_User_Cart">
                  <div className="box_User_Cart_SelectAll">
                      <input
                          type="checkbox"
                          className="btn_User_Cart_SelectAll"
                          checked={allChecked}
                          onChange={(e) => {
                            setAllChecked(e.target.checked);
                            setIsChecked(Array.from(carts, () => e.target.checked));
                            if(e.target.checked) {
                              const price = carts.reduce((acc, cart) => (cart.item.price + cart.option.addPrice) * cart.amount + acc, 0);
                              setTotalPrice(price);
                              if(price >= 50000 || price === 0) {
                                setDeliverPrice(0);
                              } else if(price >= 30000) {
                                setDeliverPrice(2500);
                              } else if(price > 0) {
                                setDeliverPrice(5000);
                              }
                            }
                            else {
                              setTotalPrice(0);
                              setDeliverPrice(0);
                            }
                          }}
                      />
                      { allChecked ? "전체 선택 해제" : "전체 선택" }
                  </div>
                  <div className="box_User_Cart_Item">
                      {
                          carts.map((cart, idx) => {
                              return <div className="box_Cart_Item" key={`cart${idx}`}>
                                  <div
                                    className="box_Cart_Delete"
                                    onClick={() => handleDelete(cart.cartPk)}
                                  >
                                    X
                                  </div>
                                  <div className="box_Cart_CheckBox">
                                      <input
                                          type="checkbox"
                                          className="btn_User_Cart_Select"
                                          checked={isChecked[idx]}
                                          onChange={(e) => {
                                            let newIsChecked = [...isChecked];
                                            newIsChecked[idx] = e.target.checked;
                                            setIsChecked(newIsChecked);
                                            const price = totalPrice +
                                              (e.target.checked ?
                                                (cart.item.price + cart.option.addPrice) * cart.amount :
                                                - (cart.item.price + cart.option.addPrice) * cart.amount
                                              );
                                            setTotalPrice(price);
                                            if(price >= 50000 || price === 0) {
                                              setDeliverPrice(0);
                                            } else if(price >= 30000) {
                                              setDeliverPrice(2500);
                                            } else if(price > 0) {
                                              setDeliverPrice(5000);
                                            }
                                          }}
                                      />
                                  </div>
                                  <div className="box_Cart_Img">
                                      <img
                                          src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${cart.item.img.split("/")[0]}`}
                                          className="btn_User_Cart_Img"
                                          alt="물품 이미지"
                                      />
                                  </div>
                                  <div className="box_Cart_Item_Info">
                                      <div className="box_Cart_Item_Title" onClick={() => navigate("/user/item", {state: {itemPk: cart.item.id}})}>
                                        {cart.item.title}
                                      </div>
                                      <div className="box_Cart_Item_Option">
                                          <div className="cart_Item_Option">
                                            {cart.option.optionInfo}
                                          </div>
                                          <div className="cart_Item_Amount">
                                            {cart.amount}개
                                          </div>
                                          <div className="cart_Item_Price">
                                            {(cart.item.price + cart.option.addPrice) * cart.amount}원
                                          </div>
                                      </div>
                                  </div>
                              </div>;
                          })
                      }
                  </div>
                  </div>
                }
            
            <div className="box_User_Cart_Info">
                <table>
                    <tr>
                        <td>물품 가격</td>
                        <td>{totalPrice} 원</td>
                    </tr>
                    <tr>
                        <td>배송비</td>
                        <td>{deliverPrice} 원</td>
                    </tr>
                    <tr>
                        <td>결제 예상 금액</td>
                        <td>{totalPrice + deliverPrice} 원</td>
                    </tr>
                    <tr>
                        <td colSpan={2}>
                            <button
                                onClick={() =>{navigate("/user/pay", {state: {carts: carts.filter((_, idx) => isChecked[idx])}})}}>구매하기</button>
                        </td>
                    </tr>                    
                </table>
            </div>
        </div>
             
        {/* <Modal
            isOpen={optionModal}
            onRequestClose={handleCloseOptionModal}
            className="modal_User_Pay_Option"
            overlayClassName="modal_User_Pay_Option_Overlay">            
            <table>
                    <tr>
                        <td>옵션</td>
                        <td>옵션 수정 선택</td>
                    </tr>
                    <tr>
                        <td>개수</td>
                        <td><input type="number"/>개</td>
                    </tr>
                    <tr>
                        <td>가격</td>
                        <td>1,153,000 원</td>
                    </tr>
                    <tr>
                        <td>
                            <button
                                onClick={handleCloseOptionModal}>
                                수정
                            </button>
                        </td>
                        <td>
                            <button
                                onClick={handleCloseOptionModal}>
                                취소
                            </button>
                        </td>
                    </tr>                    
                </table>
        </Modal> */}
    </>
}

export default UserCartComponent;