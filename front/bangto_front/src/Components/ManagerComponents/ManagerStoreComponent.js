import React, { useEffect, useState } from 'react';
import axios from "axios";
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import '../ManagerComponent.css';
import { resContent, resPage } from '../UtilComponent/ResponseData';
import PagenationComponent from "../UtilComponent/PagenationComponent";
Modal.setAppElement('#root');

const ManagerStoreComponent = () => {
    const navigate = useNavigate();
    const [storelist, setStorelist] = useState([]);
    const [storePage, setStorePage] = useState(1);
    const [storeTotalPage, setStoreTotalPage] = useState(1);
    const [selectedStore, setSelectedStore] = useState({});
    const [busiNum, setBusiNum] = useState("");
    const [storeModal, setStoreModal] = useState(false);
    const [editStoreName, setEditStoreName] = useState("");

    useEffect(()=> {handleStoreList()},[storePage]);
    
    const handleStoreList = () => {
        axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/manager/store/get-list/${storePage}`, {
            withCredentials : true,
            headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`}
        }).then((res)=> {
            setStorelist(resContent(res));
            setStorePage(storePage);
            setStoreTotalPage(resPage(res) ? resPage(res).totalPages : 1);
        }).catch((error)=>{
            console.log(error);
        })
    }

    const handleStoreModi = (store) => {
        setSelectedStore(store);
        setBusiNum(store.busiNum);
        setStoreModal(true);
    }

    const handleStoreDel = async(storeId) => {
        try {
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/manager/store/delete`, {id : storeId}, {
                withCredentials : true,
                headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`}});
            if (response.status == 200) {
                console.log("매장 삭제 성공");
                alert("매장 삭제 성공");
                handleStoreList();
            } else {
                console.error("회원 추방 실패:");
            }
        } catch (error) {
            console.error("회원 추방 오류:", error);
        }
    }

    const handleCloseStoreModal = () => {
        setSelectedStore({});
        setStoreModal(false);
    };

    const handleStoreUpdate = async (storeId) => {
        try {
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/manager/store/modify`, 
                { "id": storeId, "name":editStoreName, busiNum }, 
                {headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`}}
            );
            if (response.status === 200) {
                alert("매장 정보 수정 성공");
                setStoreModal(false);
                handleStoreList();
            } else {
                console.error("매장 정보 수정 실패");
            }
        }
        catch (error) {
            console.error("매장 정보 수정 오류:", error);
        }
    }

    return(
        <>
        <div className="box_Manager_Store_Detail">
            <div className="box_Manager_Store_Info">
                {Array.isArray(storelist) && storelist.length > 0 ? (
                    <table className="table_Manager_Store_Info">
                        <tr>
                            <th>판매자 이름</th>
                            <th>매장 이름</th>
                            <th>사업자 번호</th>
                            <th>수정</th>
                            <th>삭제</th>
                        </tr>
                        {storelist.map((store) => (
                            <tr key={store.id}>
                                <td>{store.sellerName}</td>
                                <td onClick={() => navigate(`/manager/store/info/?storename=${store.name}`)}>
                                    {store.name}
                                </td>
                                <td>{store.busiNum}</td>
                                <td>
                                    <button onClick={() => handleStoreModi(store)}>
                                        수정
                                    </button>
                                </td>
                                <td>
                                    <button onClick={() => handleStoreDel(store.id)}>
                                        삭제
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </table>
                    ) : (
                    <div>매장 없음.</div>
                )}
            </div>
            <div>
                <PagenationComponent page={storePage} setPage={setStorePage} totalPage={storeTotalPage}/>
            </div>
        </div>
         <Modal
            isOpen={storeModal}
            onRequestClose={handleCloseStoreModal}
            className="modal_Manager_Store_Modi"
            overlayClassName="modal_Manager_Store_Modi_Overlay">
            {selectedStore ? (
                <>
                    <div>
                        <table className="table_Modal_Manager_Store_Modi">
                            <tr>
                                <td>매장 이름</td>
                                <td>
                                    <input
                                        type="text"
                                        placeholder={selectedStore.name}
                                        value={editStoreName}
                                        onChange={(e) => setEditStoreName(e.target.value)}/>
                                </td>
                            </tr>
                            <tr>
                                <td>판매자 이름</td>                        
                                <td>{selectedStore.sellerName}</td> 
                            </tr>
                            <tr>
                                <td>사업자 번호</td>                        
                                <td>{selectedStore.busiNum}</td> 
                            </tr>
                        </table>
                    </div>
                    <div>
                        <button
                            className="btn_Modal_Manager_Store_Modi"
                            onClick={() => handleStoreUpdate(selectedStore.id)}>
                            수정
                        </button>
                        <button
                            className="btn_Modal_Manager_Store_Modi"
                            onClick={handleCloseStoreModal}>
                            취소
                        </button>
                    </div>
                </>
            ) : <div>선택된 매장이 없음.</div>}
        </Modal>
        </>
    )
}

export default ManagerStoreComponent;