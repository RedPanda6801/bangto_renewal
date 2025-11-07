import axios from "axios";
import { useEffect, useState } from "react";
import { StartTimeFormat, EndTimeFormat, CategoryType } from "../UtilComponent/DataFormat";
import { resContent } from "../UtilComponent/ResponseData";
import { useNavigate } from "react-router-dom";

const ManagerGroupComponent = () => {
    const [groupEventList, setGroupEventList] = useState([]);
    const [startInputDate, setStartInputDate] = useState("");
    const [endInputDate, setEndInputDate] = useState("");
    const [selectedEvent, setSelectedEvent] = useState("");
    const [groupItemList, setGroupItemList] = useState([]);
    const navigate = useNavigate();
    // const [eventPage, setEventPage] = useState(1);
    // const [totalEventPage, setTotalEventPage] = useState(1);

    useEffect(()=>{
        handleEventList();
    },[])

    const handleAddEvent = async () => {
        try{
            const startDate = StartTimeFormat(startInputDate);
            const endDate = EndTimeFormat(endInputDate);
            console.log(startDate);
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/manager/group-buy/add`,{
                startDate, endDate
            },{headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`}})

            if(response.status === 200){
                alert("등록 완료");
            }
        }catch(error){
            console.log(error);
            alert("등록 실패");
        }
    }

    const handleEventList = async() => {
        try {  
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-buy/get-list`,{
                withCredentials : true,
                headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`}});

            if (response.status == 200) {
                console.log(response);
                setGroupEventList(resContent(response));
            } 
            else {
                console.error("공동 구매 기간 가져오기 실패:");
            }  
        }
        catch (error) {
            if(error.status == 401){
                alert("토큰 만료");
                navigate("/", { state: { category: "Main" } });
            } else console.error("공동 구매 기간 가져오기 오류:", error);
        }
    }

    const handleSelectedEvent = async (eventId) => {
        try {
            if(eventId == "") return;
            setSelectedEvent(eventId);
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/group-item/event/get-list/${eventId}`, {
                withCredentials : true,
                headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`}});

            if (response.status == 200) {
                console.log(response);
                setGroupItemList(resContent(response));
            } 
            else {
                console.error("공동 구매 기간 가져오기 실패:");
            }  
        }
        catch (error) {
            if(error.status == 401){
                alert("토큰 만료");
                navigate("/", { state: { category: "Main" } });
            } else console.error("공동 구매 기간 가져오기 오류:", error);
        }
    }

    return <div className="box_Manager_Group_Item_Detail">
                    <div className="box_Manager_Group_Item_Set">
                        <input
                            className="manager_Group_Item_Date"
                            value={startInputDate}
                            onChange={(e) => setStartInputDate(e.target.value)}
                            type="date"/>
                        <input
                            className="manager_Group_Item_Date"
                            value={endInputDate}
                            onChange={(e) => setEndInputDate(e.target.value)}
                            type="date"/>
                        <button onClick={() => handleAddEvent()}>
                            등록
                        </button>
                    </div>
                    <div>
                        {groupEventList && Array.isArray(groupEventList) ? (
                            <>
                            <select onChange={(e) => handleSelectedEvent(e.target.value)}>
                                <option value="">이벤트를 선택하세요</option>
                                {
                                    groupEventList.map((event)=>(   
                                    <option value={event.id}>{event.startDate} ~ {event.endDate}</option>
                                    ))
                                }
                            </select>
                            <div className="box_Manager_Group_Item_Info">
                            <table className="table_Manager_Group_Item_Info">
                            <tr>
                                <th>상품명</th>
                                <th>카테고리</th>
                                <th>이미지</th>
                                <th>옵션 정보</th>
                                <th>상품 가격</th>
                                <th>인당 제한 개수</th>
                                <th>최대 수량 / 현재 수량</th>
                            </tr>
                            { groupItemList && Array.isArray(groupItemList) ? 
                                groupItemList.map((groupItem) => 
                                    <tr>
                                        <td>{groupItem.item.title}</td>
                                        <td>{CategoryType[groupItem.item.category]}</td>
                                        <td><img style={{maxWidth : "200px"}} src={`${process.env.REACT_APP_IMG_PUBLIC_URI}/03_upload/${groupItem.item.img.split("/")[0]}`}/></td>
                                        <td>{groupItem.option.optionInfo}</td>
                                        <td>{groupItem.item.price+groupItem.option.addPrice}</td>
                                        <td>{groupItem.limitPerBuyer}</td>
                                        <td>{`${groupItem.maxAmount}/${groupItem.nowAmount?groupItem.nowAmount:groupItem.maxAmount}`}</td>
                                    </tr>
                                )
                            :  
                                <tr>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                    <td>-</td>
                                </tr>
                            }
                            </table>
                            </div> 
                            </>    
                            ) : <div>이벤트를 등록하세요</div>
                        }
                    </div>
                    <div>
                        {/* {authPagination()} */}
                    </div>
                </div>
}

export default ManagerGroupComponent;