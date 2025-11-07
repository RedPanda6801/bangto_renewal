import axios from "axios";
import { useNavigate } from 'react-router-dom';
import '../ManagerComponent.css';
import { useEffect, useState } from "react";
import { resPage , resContent } from '../UtilComponent/ResponseData';
import PagenationComponent from "../UtilComponent/PagenationComponent";
import Modal from 'react-modal';
Modal.setAppElement('#root');

const ManagerUserComponent = () => {
    const navigate = useNavigate();
    const [userPage, setUserPage] = useState(1);
    const [userTotalPage, setUserTotalPage] = useState(1);
    const [userlist, setUserlist] = useState([]);
    const [selectedUser, setSeletedUser] = useState({});
    const [modal, setModal] = useState(false);
    const [editName, setEditName] = useState("");
    const [editPhone, setEditPhone] = useState("");
    const [editAddr, setEditAddr] = useState("");

    useEffect(() => {handleUserList()},[userPage])

    const handleUserList = () => {
        axios.get(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/manager/user/get-list/${userPage}`, {
            withCredentials : true,
            headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`,
        }}).then((res)=> {
            const rescontent = resContent(res);
            for(const content of rescontent) {
                if(content["addr"] !== null && content["addr"].split("!!")[0] !== "undefined") {
                    content["addr"] = content["addr"].replaceAll("!!", " ");
                }
            }
            setUserlist(rescontent);
            setUserPage(userPage);
            setUserTotalPage(resPage(res) ? resPage(res).totalPages : 1);
        }).catch((error) => {
            if(error.status === 401){
                alert("토큰 만료 오류 : 다시 로그인하세요.");
                navigate("/", { state: { category: "Main" } });
            }
            console.error("회원 정보 가져오기 오류:", error);
        })
    }

    const handleUserModi = (user) => {
        setSeletedUser(user);
        setModal(true);
    }
    
    const handleUserDel = async (userId) => {
        if(window.confirm("회원을 삭제 하시겠습니까?")) {
            try {    
                const response = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/manager/user/delete/${userId}`, {}, {
                        withCredentials : true,
                        headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`,
                }
                });

                if (response.status == 200) {
                    console.log("회원 추방 성공");
                    alert("회원 추방 성공");
                    handleUserList();
                } else {
                    console.error("회원 추방 실패:");
                }
            } catch (error) {
                console.error("회원 추방 오류:", error);
            }
        }
        // 삭제 안하면 아무것도 안함함
    }
    
    const handleUserModiModal = async(selectedUser) => {
        try {   
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_SERVER_PORT}/manager/user/modify/${selectedUser.id}`, 
                { name : editName, addr : editAddr, phone : editPhone}, {
                    withCredentials : true,
                    headers: { "Authorization": `Bearer ${localStorage.getItem("token")}`,
            }});
            if (response.status == 200) {
                alert("회원 정보 수정 성공");
                handleUserList();
            } else {
                console.error("회원 정보 수정 실패:");
            }
        } catch (error) {
            console.error("회원 정보 수정 오류:", error);
        }
        handleCloseModal();
    }
    
    const handleCloseModal = () =>
    {
        setModal(false);
        setSeletedUser({});
    }


    return (
        <>
        <div className="box_Manager_User_Detail">
            <div className="box_Manager_User_Info">
                { userlist && Array.isArray(userlist) ? (
                    <table className="table_Manager_User_Info">
                    <tr>
                        <th>이메일</th>
                        <th>별명</th>
                        <th>전화번호</th>
                        <th>주소</th>
                        <th>수정</th>
                        <th>삭제</th>
                    </tr>
                        {userlist.map((user) => (
                            <tr>   
                                <td>{user.email}</td>
                                <td>{user.name}</td>
                                <td>{user.phone}</td>
                                <td>{(user.addr != "undefined!!!!undefined") ? user.addr : ""}</td>
                                <td>
                                    <button onClick={() => handleUserModi(user)}>수정</button>
                                </td>
                                <td>
                                    <button onClick={() => handleUserDel(user.id)}>삭제</button>
                                </td>
                            </tr>
                        ))}
                    </table>
                ) : <div>유저 없음</div>
                }
            </div>
            <div>
                <PagenationComponent page={userPage} setPage={setUserPage} totalPage={userTotalPage}/>
            </div>
        </div>
        <Modal 
            isOpen={modal} 
            onRequestClose={handleCloseModal} 
            className="modal_Manager_User_Modi" 
            overlayClassName="modal_Manager_User_Modi_Overlay">
            <div>
                {selectedUser && Object.keys(selectedUser).length !== 0 ? (
                    <table className="table_Modal_Manager_User_Modi">
                        <tr>
                            <td>이메일</td>
                            <td>{}</td>
                        </tr>
                        <tr>
                            <td>별명</td>
                            <td>
                                <input 
                                    type="text"
                                    placeholder={selectedUser.name}
                                    value={editName}
                                    onChange={(e) => setEditName(e.target.value)}/>
                            </td>
                        </tr>
                        <tr>
                            <td>전화번호</td>
                            <td>
                                <input 
                                    type="text"
                                    placeholder={selectedUser.phone}
                                    value={editPhone}
                                    onChange={(e) => setEditPhone(e.target.value)}/></td>
                        </tr>
                        <tr>
                            <td>주소</td>
                            <td>
                                <input 
                                    type="text"
                                    placeholder={selectedUser.addr}
                                    value={editAddr}
                                    onChange={(e) => setEditAddr(e.target.value)}/>
                            </td>
                        </tr>
                    </table>
                ) 
                : <div>선택된 유저가 없음</div>
                }
            </div>
            <div>
                <button 
                    className="btn_Modal_Manager_User_Modi"
                    onClick={() => handleUserModiModal(selectedUser)}
                    >
                    수정
                </button>
                <button 
                    className="btn_Modal_Manager_User_Modi"
                    onClick={handleCloseModal}>
                    취소
                </button>
            </div>
        </Modal>
        </>
    );
}

export default ManagerUserComponent;