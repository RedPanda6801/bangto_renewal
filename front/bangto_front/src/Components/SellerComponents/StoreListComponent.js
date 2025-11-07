import { useEffect, useState } from "react";
import "../StoreComponent.css";
import { useNavigate } from "react-router-dom";
import { resContent, resPage } from "../UtilComponent/ResponseData";
import axios from "axios";
import StoreModiComponent from "./StoreModiComponent";
import StoreItemRegisterComponent from "./StoreItemRegisterComponent";
import StoreAddComponent from "./StoreAddComponent";
import { CategoryType } from "../UtilComponent/DataFormat";
import StoreGroupItemRegisterComponent from "./StoreGroupItemRegisterComponent";
import PagenationComponent from "../UtilComponent/PagenationComponent";

const StoreListComponent = (props) => {
  const [storeInfo, setStoreInfo] = useState({});
  const [itemPage, setItemPage] = useState(1);
  const [totalPage, setTotalPage] = useState(1);
  const [itemlist, setItemlist] = useState([]);
  const [modiModal, setModiModal] = useState(false);
  const [groupItemModal, setGroupItemModal] = useState(false);
  const [addStoreModal, setAddStoreModal] = useState(false);
  const [addItemModal, setAddItemModal] = useState(false);
  const [selectedItem, setSelectedItem] = useState(-1);
  const [option, setOption] = useState({});
  const [addOptionView, setAddOptionView] = useState(false);
  const [optionInfo, setOptionInfo] = useState("");
  const [optionPrice, setOptionPrice] = useState(0);
  const [optionAmount, setOptionAmount] = useState(0);
  const SERVER_PORT = process.env.REACT_APP_BACKEND_SERVER_PORT;

  useEffect(() => {
    handleItemList();
  }, [props.storeInfo, itemPage]);

  const handleItemDelete = async (id) => {
    if (window.confirm("물품을 삭제하시겠습니까?")) {
      try {
        const response = await axios.post(
          `${SERVER_PORT}/item/delete`,
          { id, storePk: props.storeInfo.id },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (response.status === 200) {
          alert("삭제 성공");
          handleItemList();
        }
      } catch (error) {
        alert("삭제 실패");
        console.log(error);
      }
    }
  };

  const handleItemList = async () => {
    try {
      setStoreInfo(props.storeInfo); //ok
      if (props.storeInfo && props.storeInfo.id) {
        const itemResponse = await axios.get(
          `${SERVER_PORT}/item/get-itemlist/${props.storeInfo.id}/${itemPage}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        setItemlist(resContent(itemResponse));
        setTotalPage(resPage(itemResponse).totalPages);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleOptionDelete = async (optionId) => {
    try {
      const response = await axios.post(
        `${SERVER_PORT}/item/option/delete`,
        { id: optionId },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      if (response.status == 200) {
        alert("옵션 삭제 성공");
        handleItemList();
      }
    } catch (error) {
      alert("옵션 삭제 실패");
      console.log(error);
    }
  };

  const handleOptionAdd = async (itemPk) => {
    try {
      const response = await axios.post(
        `${SERVER_PORT}/item/option/add`,
        { itemPk, optionInfo, addPrice: optionPrice, amount: optionAmount },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      if (response.status == 200) {
        alert("옵션 추가 성공");
        handleItemList();
        setAddOptionView(false);
      }
    } catch (error) {
      alert("옵션 추가 실패");
      console.log(error);
    }
  };

  const toggleAddOptionView = () => {
    setAddOptionView((prev) => !prev);
  };

  const handleOptionChange = (itemId, value) => {
    setOption((prev) => ({ ...prev, [itemId]: value })); // 특정 상품의 옵션만 변경
  };

  return (
    <>
      <div className="box_Store_Detail">
        <div className="box_Store_Info">
          <table>
            <tr>
              <th>판매자 이름</th>
              <th>매장 이름</th>
              <th>사업자 번호</th>
            </tr>
            <tr>
              <td>{props.sellerName}</td>
              <td>{storeInfo.name}</td>
              <td>{storeInfo.busiNum}</td>
            </tr>
          </table>
          <button onClick={() => setModiModal(true)}>매장 수정</button>
          <button onClick={() => setAddStoreModal(true)}>매장 추가</button>
        </div>
        <button className="btn_Store_Add" onClick={() => setAddItemModal(true)}>
          판매 물품 등록
        </button>
        <div className="box_Item_Info">
          {itemlist && Array.isArray(itemlist) && itemlist.length !== 0 ? (
            <table>
              <tr>
                <th>상품명</th>
                <th>상품 설명</th>
                <th>카테고리</th>
                <th>상품 가격</th>
                <th>옵션 보기</th>
                <th>공동구매 등록</th>
                <th>상품 삭제</th>
              </tr>
              {itemlist.map((item) => (
                <tr>
                  <td
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                    }}
                  >
                    <img
                      style={{ width: "200px" }}
                      src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${
                        item.img.split("/")[0]
                      }`}
                    />
                    <div>{item.title}</div>
                  </td>
                  <td>{item.content}</td>
                  <td>{CategoryType[item.category]}</td>
                  <td>{item.price}</td>
                  <td>
                    {Array.isArray(item.options) && item.options.length > 0 ? (
                      <>
                        <select
                          onChange={(e) =>
                            handleOptionChange(item.id, e.target.value)
                          }
                        >
                          <option value={-1}>옵션을 선택하세요</option>
                          {item.options.map((ele, ind) => (
                            <option value={ind}>{ele.optionInfo}</option>
                          ))}
                        </select>
                        {option[item.id] && option[item.id] != -1 ? (
                          <>
                            <div>
                              {item.options[option[item.id]].addPrice}원{" "}
                              {item.options[option[item.id]].amount}개
                            </div>
                            <div>
                              <button
                                onClick={() =>
                                  handleOptionDelete(
                                    item.options[option[item.id]].id
                                  )
                                }
                              >
                                삭제
                              </button>
                              <button onClick={() => toggleAddOptionView()}>
                                추가
                              </button>
                            </div>
                            {addOptionView ? (
                              <div>
                                <input
                                  placeholder="옵션 정보"
                                  onChange={(e) =>
                                    setOptionInfo(e.target.value)
                                  }
                                ></input>
                                <input
                                  placeholder="옵션 가격"
                                  onChange={(e) =>
                                    setOptionPrice(e.target.value)
                                  }
                                ></input>
                                <input
                                  placeholder="옵션 수량"
                                  onChange={(e) =>
                                    setOptionAmount(e.target.value)
                                  }
                                ></input>
                                <button
                                  onClick={() => handleOptionAdd(item.id)}
                                >
                                  등록
                                </button>
                              </div>
                            ) : null}
                          </>
                        ) : (
                          <div></div>
                        )}
                      </>
                    ) : (
                      <div>옵션 없음</div>
                    )}
                  </td>
                  <td>
                    <button
                      onClick={() => {
                        setSelectedItem(item);
                        setGroupItemModal(true);
                      }}
                    >
                      공동구매 등록
                    </button>
                  </td>
                  <td>
                    <button onClick={() => handleItemDelete(item.id)}>
                      삭제
                    </button>
                  </td>
                </tr>
              ))}
            </table>
          ) : (
            <div style={{ textAlign: "center" }}>물건이 없습니다.</div>
          )}
        </div>
        <PagenationComponent page={itemPage} setPage={setItemPage} totalPage={totalPage}/>
      </div>
      <StoreGroupItemRegisterComponent
        modal={groupItemModal}
        setModal={setGroupItemModal}
        store={props.storeInfo}
        sellerName={props.sellerName}
        item={selectedItem}
      />
      <StoreItemRegisterComponent
        modal={addItemModal}
        setModal={setAddItemModal}
        store={props.storeInfo}
        sellerName={props.sellerName}
      />
      <StoreAddComponent
        modal={addStoreModal}
        setModal={setAddStoreModal}
        store={props.storeInfo}
        sellerName={props.sellerName}
      />
      <StoreModiComponent
        modal={modiModal}
        setModal={setModiModal}
        store={props.storeInfo}
        sellerName={props.sellerName}
      />
    </>
  );
};

export default StoreListComponent;
