import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { resContent } from '../UtilComponent/ResponseData';
import axios from 'axios';
import '../StoreGroupItemRegisterComponent.css'; 
import Modal from 'react-modal';
Modal.setAppElement('#root');

const StoreGroupItemRegisterComponent = (props) => {   
  const [limitPerBuyer, setLimitPerBuyer] = useState(0);
  const [maxAmount, setMaxAmount] = useState(0);
  const [option, setOption] = useState({});
  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState({});
  const SERVER_PORT = process.env.REACT_APP_BACKEND_SERVER_PORT;

  useEffect(()=> {
    if(props.item && props.item.options){
      setOption(props.item.options[0]);
    }
    handleGetEventInfo();
  }, [props.item, props.modal])
  
  const handleGetEventInfo = async () => {
    try{
      const response = await axios.get(`${SERVER_PORT}/group-buy/get-choose-list`, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("token")}`,
        }
      })
      setEvents(resContent(response));
      setSelectedEvent(resContent(response)[0])
    }catch(error){
        console.log(error);
    }
  }

  const handelAddGroupItem = async () => {
    try{
      if(limitPerBuyer == 0 || maxAmount == 0 || !selectedEvent || !props.item.id || !option){
        alert("필수 정보를 입력하세요");
        return;
      }
      const dto = {
        itemPk : props.item.id,
        limitPerBuyer,
        maxAmount,
        eventPk : selectedEvent.id,
        optionPk : option.id,
      }
      console.log(dto);
      const response = await axios.post(`${SERVER_PORT}/group-item/add`, dto, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("token")}`,
        }
      })

      if(response.status == 200){
        alert("공동 구매 물품 등록 성공");
        handleCloseModal();
      }
    }catch(error){
      alert("공동 구매 물품 등록 실패");
      console.log(error);
    }
  };

  const handleCloseModal = () => {
    props.setModal(false);
  }


  return  (
     <Modal 
        isOpen={props.modal} 
        onRequestClose={handleCloseModal} 
        className="modal_Manager_User_Modi" 
        overlayClassName="modal_Manager_User_Modi_Overlay">
        <div className="layout_Store_Group_Item_Regi">
        <div className="box_Store_Group_Item_Add">
          <input 
            className="Group_Item_Add_Store" 
            type="text" 
           // defaultValue={storeName}
            disabled/>
          <div className="Item_Add_Title">공동 구매 정보</div>
          <table className="Group_Item_Add_Table">
            <tr><td>구매 기간</td>
            <td>
            {events && Array.isArray(events) && events.length > 0 ? <select onChange={(e) => setSelectedEvent(e.target.value)}>
                  {events.map((event)=> <option value={event.id}>{event.startDate} ~ {event.endDate}</option>)}
                </select>: <div>공동 구매 기간이 아닙니다.</div>}
            </td>
            </tr>
          </table>
          {props.item? (
          <>
            <table className="Group_Item_Add_Table">
            <tr><td>물품 이름</td>
              <td>
                <input 
                  type="text" 
                  defaultValue={props.item.title}
                  disabled/>
              </td>
            </tr>
          </table>
          <table className="Group_Item_Add_Table">
            <tr><td>1인 제한 수량</td>
              <td>
                <input 
                  type="number"
                  className="Item_Limit_Amount_Per"
                  value={limitPerBuyer}
                  onChange={(e) => setLimitPerBuyer(e.target.value)}
                  />
              </td>
            </tr>
          </table>
          <table className="Group_Item_Add_Table">
            <tr><td>선택 옵션</td>
              <td>
                {props.item.options? <select onChange={(e) => setOption(e.target.value)}>
                  {props.item.options.map((option)=> <option value={option.id}>{option.optionInfo}</option>)}
                </select>: <div></div>}
              </td>
            </tr>
          </table>
          <table className="Group_Item_Add_Table">
            <tr><td>최대 수량</td>
              <td>
                <input 
                type="number"
                className="Item_Limit_Amountt"
                value={maxAmount}
                onChange={(e) => setMaxAmount(e.target.value)}
                />
              </td>
            </tr>
          </table>
          <div className="box_Btn_Item_Add">
            <input 
              className="btn_Group_Item_Add"
              type="button" 
              value="공동 구매 등록"
              onClick={handelAddGroupItem}/>
            <input 
              className="btn_Group_Item_Add"
              type="button" 
              value="취소"
              onClick={() => handleCloseModal()}/>
          </div>
          </>): <div></div>}  
        </div>
      </div>
    </Modal>
  );
};

export default StoreGroupItemRegisterComponent;
