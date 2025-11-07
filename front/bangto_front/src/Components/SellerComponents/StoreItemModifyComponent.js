import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { CategoryType } from "../UtilComponent/DataFormat";
import axios from "axios";
import "../StoreItemModifyComponent.css";
import Modal from "react-modal";
Modal.setAppElement("#root");

const StoreItemModifyComponent = (props) => {
  const navigate = useNavigate();
  const [images, setImages] = useState([]);
  const [files, setFiles] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [amount, setAmount] = useState(0);
  const [category, setCategory] = useState(CategoryType.Clothing);
  const [optionInfo, setOptionInfo] = useState("");
  const [addPrice, setAddPrice] = useState(0);
  const [price, setPrice] = useState(0);
  const [options, setOptions] = useState([]);

  useEffect(() => {
    // if(props.item){
    //   const filesArray = Array.from(e.target.files);
    //   setFiles(filesArray);
    //   filesArray.forEach((file) => {
    //     const reader = new FileReader();
    //     reader.onloadend = () => {
    //       setImages((prevImages) => [...prevImages, reader.result]);
    //     };
    //     reader.readAsDataURL(file);
    //   });
    // }
  }, [props.item]);

  const handleImageChange = (e) => {
    if (e.target.files.length > 0) {
      const filesArray = Array.from(e.target.files);
      setFiles(filesArray);
      filesArray.forEach((file) => {
        const reader = new FileReader();
        reader.onloadend = () => {
          setImages((prevImages) => [...prevImages, reader.result]);
        };
        reader.readAsDataURL(file);
      });
    }
  };

  const handleAddOption = () => {
    if (optionInfo && addPrice && amount) {
      setOptions([...options, { optionInfo, addPrice, amount }]);
      setOptionInfo("");
      setAddPrice("");
    } else {
      alert("옵션 정보와 가격을 모두 입력하세요.");
    }
  };

  const handleDeleteOption = (index) => {
    setOptions(options.filter((_, i) => i !== index));
  };

  const handelAddItem = async () => {
    try {
      const dto = {
        storePk: props.store.id,
        title,
        price,
        content,
        category,
        options,
      };
      console.log(dto);
      const formData = new FormData();
      formData.append(
        "dto",
        new Blob([JSON.stringify(dto)], { type: "application/json" })
      );
      files.forEach((file) => {
        formData.append("files", file);
      });
      const response = await axios.post(
        `${process.env.REACT_APP_BACKEND_SERVER_PORT}/item/add-item`,
        formData,
        {
          withCredentials: true,
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (response.status == 200) {
        alert("물품등록 성공");
        navigate(0);
      } else {
        console.error("물품등록 실패:");
      }
    } catch (error) {
      console.error("물품등록 오류:", error);
    }
  };

  const handleCloseModal = () => {
    props.setModal(false);
  };

  return (
    <Modal
      isOpen={props.modal}
      onRequestClose={handleCloseModal}
      className="modal_Manager_User_Modi"
      overlayClassName="modal_Manager_User_Modi_Overlay"
    >
      <div className="layout_Store_Item_Regi">
        <div className="box_Store_Item_Add">
          <input className="Item_Add_Store" type="text" disabled />
          <div className="Item_Add_Title">상품정보</div>
          {props.item ? (
            <>
              <table className="Item_Add_Table">
                <tr>
                  <td>상품이미지</td>
                  <td>
                    <div className="Item_Add_Img">
                      <input
                        type="file"
                        accept="image/*"
                        multiple
                        onChange={handleImageChange}
                        id="btn_Img_Upload"
                      />
                      <label htmlFor="btn_Img_Upload">
                        {/* {images && images.length > 0 ? images.map((image)=> ( */}
                        {props.item.img ? (
                          props.item.img.split("/").map((ele) => {
                            {
                              console.log(
                                `${process.env.REACT_APP_IMG_PUBLIC_URI}/${ele}`
                              );
                            }
                            <img
                              src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/${ele}`}
                              alt="업로드된 이미지 미리보기"
                            />;
                          })
                        ) : (
                          <div></div>
                        )}
                        {/* )) : (
                      <div>이미지 등록</div>
                    )} */}
                      </label>
                    </div>
                  </td>
                </tr>
              </table>
              <table className="Item_Add_Table">
                <tr>
                  <td>상품명</td>
                  <td>
                    {console.log(props.item)}
                    <input
                      type="text"
                      className="Item_Add_Name"
                      placeholder={props.item.title}
                      defaultValue={props.item.title}
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                    />
                  </td>
                </tr>
              </table>
              <table className="Item_Add_Table">
                <tr>
                  <td>카테고리</td>
                  <td>
                    <select
                      value={category}
                      onChange={(e) => setCategory(e.target.value)}
                    >
                      {Object.keys(CategoryType).map((key) => (
                        <option value={key}>{CategoryType[key]}</option>
                      ))}
                    </select>
                  </td>
                </tr>
              </table>
              <table className="Item_Add_Table">
                <tr>
                  <td>가격</td>
                  <td>
                    <input
                      type="number"
                      className="Item_Add_Price"
                      placeholder={props.item.price}
                      defaultValue={props.item.price}
                      onChange={(e) => setPrice(e.target.value)}
                    />
                  </td>
                </tr>
              </table>
              <table
                className={`Item_Add_Table_Option ${
                  options.length + 1 > 1 ? "Item_Add_Table_Expanded" : ""
                }`}
              >
                <tr>
                  <td rowSpan={options.length + 1}>옵션</td>
                  <td>
                    <input
                      type="text"
                      placeholder="옵션 정보"
                      value={optionInfo}
                      onChange={(e) => setOptionInfo(e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="number"
                      placeholder="추가 가격"
                      min="0"
                      value={addPrice}
                      onChange={(e) => setAddPrice(e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="number"
                      className="Item_Add_Amount"
                      placeholder="물건 개수"
                      value={amount}
                      onChange={(e) => setAmount(e.target.value)}
                    />
                  </td>
                  <td>
                    <button onClick={handleAddOption}>추가</button>
                  </td>
                </tr>
                {options.map((option, index) => (
                  <tr key={index}>
                    <td>{option.optionInfo}</td>
                    <td>{option.addPrice}원</td>
                    <td>{option.amount}개</td>
                    <td>
                      <button onClick={() => handleDeleteOption(index)}>
                        삭제
                      </button>
                    </td>
                  </tr>
                ))}
              </table>
              <table className="Item_Add_Table">
                <tr>
                  <td>내용</td>
                  <td>
                    <textarea
                      className="Item_Add_Content"
                      placeholder={props.item.content}
                      value={props.item.content}
                      onChange={(e) => setContent(e.target.value)}
                    ></textarea>
                  </td>
                </tr>
              </table>
              <div>
                <input
                  className="btn_Item_Add"
                  type="button"
                  value="물품 등록"
                  onClick={handelAddItem}
                />
                <input
                  className="btn_Item_Add"
                  type="button"
                  value="취소"
                  onClick={() => handleCloseModal()}
                />
              </div>
            </>
          ) : (
            <div></div>
          )}
        </div>
      </div>
    </Modal>
  );
};

export default StoreItemModifyComponent;
