import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../UserPayComponent.css";
import axios from "axios";
import { resContent } from "../UtilComponent/ResponseData";
import UserGroupItemPayModalComponent from "./UserGroupItemPayModalComponent";
import Modal from "react-modal";
Modal.setAppElement("#root");

const UserGroupItemPayComponent = () => {
  const navigate = useNavigate();
  const [payModal, setPayModal] = useState(false);
  const [keyword, setKeyword] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [addressList, setAddressList] = useState([]);
  const [isAddressSearchOpen, setIsAddressSearchOpen] = useState(false);
  const [totalPage, setTotalPageNum] = useState(1);
  const [visiblePages, setPageNums] = useState([]);
  const [selectedAddress, setSelectedAddress] = useState("");
  const [detailAddr, setDetailAddr] = useState("");
  const [wallet, setMyWallet] = useState({});
  const [payItem, setPayItem] = useState({});
  const [user, setUser] = useState({});
  const [discount, setDiscount] = useState(0.9);
  const params = new URLSearchParams(window.location.search);
  const amount = params.get("amount");
  const itemId = params.get("itemid");

  useEffect(() => {
    const groupItem = JSON.parse(localStorage.getItem("group-item"));
    if (groupItem == undefined) return;
    setPayItem(groupItem);
    axios
      .get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/wallet/my/get-info`, {
        withCredentials: true,
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      })
      .then((res) => {
        const wallet = resContent(res);
        setMyWallet(wallet);
        setUser(wallet.user);
        const address = wallet.user.addr.split("!!");
        setSelectedAddress({
          roadAddr: address[0] == "undefined" ? null : address[0],
          zipNo: address[2] == "undefined" ? null : address[2]
        });
        culDiscount();
        
      })
      .catch((err) => {
        if (err.status == 401) {
          alert("로그인 하세요.");
          navigate("/login");
        }
      });
  }, [amount]);

  const culDiscount = () => {
    const limit = JSON.parse(localStorage.getItem("group-item")).limitPerBuyer;
    if(limit > amount) setDiscount(0.9);
    else setDiscount(0.75);
  }
  const getAddr = (page = 1) => {
    const apiUrl = `https://business.juso.go.kr/addrlink/addrLinkApiJsonp.do?currentPage=${page}&countPerPage=10&keyword=${encodeURIComponent(
      keyword
    )}&confmKey=devU01TX0FVVEgyMDI1MDIwNDEzMzEwMDExNTQ0MzY=&resultType=json&callback=handleApiResponse`;

    window.handleApiResponse = (data) => {
      if (data.results.common.errorCode !== "0") {
        switch (data.results.common.errorCode) {
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
            alert(
              "주소가 너무 깁니다.(한글 40자, 영문, 숫자 80자 이하로 입력)"
            );
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
      } else {
        setAddressList(data.results.juso);
        setIsAddressSearchOpen(true);
        setTotalPageNum(Math.ceil(data.results.common.totalCount / 10));
        updatePageNums(page, Math.ceil(data.results.common.totalCount / 10));
      }
    };

    const script = document.createElement("script");
    script.src = apiUrl;
    document.body.appendChild(script);
  };

  // 주소창 페이지 바꾸기
  const handlePageChange = (page) => {
    setCurrentPage(page);
    getAddr(page);
    updatePageNums(page, totalPage);
  };

  // 선택한 주소 저장
  const handleAddressSelect = (address) => {
    setSelectedAddress(address);
    setIsAddressSearchOpen(false);
  };

  // 페이지 번호 설정
  const updatePageNums = (currentPage, totalPage) => {
    const pages = [];
    const range = 10;
    let start = Math.max(currentPage - 5, 1);
    let end = Math.min(start + range - 1, totalPage);

    if (end - start < range) {
      start = Math.max(end - range + 1, 1);
    }

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    setPageNums(pages);
  };

  const handleDeliverCost = () => {
    let cost = 0;
    const price = Math.round(
      (payItem.item.price + payItem.option.addPrice) * 0.9 * amount
    );

    if (price > 50000) cost = 0;
    else if (price > 30000) cost = 2500;
    else cost = 5000;
    return cost;
  };

  const handleTotalCost = () => {
    return (
      Math.round(
        (payItem.item.price + payItem.option.addPrice) * discount * amount
      ) + handleDeliverCost()
    );
  };

  const handleSetPayModal = () => {
    if (addressList == undefined || addressList.length <= 0) {
      alert("주소를 입력하세요");
    } else {
      setPayModal(true);
    }
  };

  return (
    <>
      <div className="layout_User_Pay">
        <div className="box_User_Pay">
          {payItem !== undefined && Object.keys(payItem).length > 0 ? (
            <>
              <table className="box_User_Pay_Items">
                <tr>
                  <td rowSpan={2}>
                    <img
                      src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${
                        payItem.item.img.split("/")[0]
                      }`}
                      className="btn_User_Pay_Img"
                    />
                  </td>
                  <td colSpan={2}>{payItem.item.content}</td>
                </tr>
                <tr>
                  <td></td>
                  <td>옵션 : {payItem.option.optionInfo}</td>
                  <td>개수 : {amount} 개</td>
                </tr>
              </table>
              <line className="div_line"></line>
              <div>
                <input
                  className="User_Pay_Addr"
                  type="text"
                  value={keyword}
                  onChange={(e) => setKeyword(e.target.value)}
                  placeholder="주소 입력"
                />
                <button
                  className="User_Pay_Addr_Search"
                  onClick={() => {
                    setCurrentPage(1);
                    getAddr(1);
                  }}
                >
                  검색
                </button>
              </div>
              <table>
                <tr>
                  <td>
                    <div
                      className="User_Pay_Addr_Road"
                      contentEditable={false}
                      tabindex="0"
                    >
                      {selectedAddress
                        ? selectedAddress.roadAddr
                        : "도로명 주소"}
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
                      placeholder="상세 주소"
                    />
                  </td>
                </tr>
                <tr>
                  <td>
                    <input
                      className="User_Pay_Addr_Zip"
                      type="text"
                      placeholder="우편번호"
                      value={selectedAddress ? selectedAddress.zipNo : ""}
                      readOnly
                    />
                  </td>
                </tr>
              </table>
              <line className="div_line"></line>
              {wallet != undefined || Object.keys(wallet).length > 0 ? (
                <>
                  <table className="User_Pay_Cash">
                    <tr>
                      <td>총 상품 가격</td>
                      <td>
                        {`${Math.round(
                          (payItem.item.price + payItem.option.addPrice) *
                            discount * amount
                        )} 원 (공동구매 ${Math.round((1-discount) * 100)}% 할인)`}
                      </td>
                    </tr>
                    <tr>
                      <td>배송비</td>
                      <td>{handleDeliverCost()}원</td>
                    </tr>
                    <tr>
                      <td>총 결제 금액</td>
                      <td>{handleTotalCost()} 원</td>
                    </tr>
                  </table>
                  <div>
                    <button
                      className="btn_User_Pay_Cash"
                      onClick={() => handleSetPayModal()}
                    >
                      결제하기
                    </button>
                    <button
                      className="btn_User_Pay_Cash"
                      onClick={() => navigate(-1)}
                    >
                      뒤로가기
                    </button>
                  </div>
                </>
              ) : (
                <div>지갑을 불러올 수 없습니다.</div>
              )}
            </>
          ) : (
            <div>물건을 불러올 수 없습니다.</div>
          )}
        </div>
      </div>
      <Modal
        isOpen={isAddressSearchOpen}
        onRequestClose={() => setIsAddressSearchOpen(false)}
        contentLabel="주소 선택 창"
        className="modal_Addr"
        overlayClassName="modal_Overlay_Addr"
        ariaHideApp={false}
      >
        <h3>주소 선택</h3>
        <button
          className="modal_Addr_Close"
          onClick={() => setIsAddressSearchOpen(false)}
        >
          X
        </button>
        <table>
          <tr>
            <th>도로명 주소</th>
            <th>우편번호</th>
          </tr>
          {addressList.map((address, index) => (
            <tr key={index} onClick={() => handleAddressSelect(address)}>
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
              className={currentPage == page ? "active" : ""}
            >
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
      {payItem !== undefined && Object.keys(payItem).length > 0 ? (
        <UserGroupItemPayModalComponent
          payModal={payModal}
          setPayModal={setPayModal}
          wallet={wallet}
          totalCost={handleTotalCost(payItem)}
          address={`${selectedAddress.roadAddr} ${detailAddr}`}
        ></UserGroupItemPayModalComponent>
      ) : null}
    </>
  );
};

export default UserGroupItemPayComponent;
