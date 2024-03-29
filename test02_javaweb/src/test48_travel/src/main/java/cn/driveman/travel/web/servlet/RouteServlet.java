package cn.driveman.travel.web.servlet;

import cn.driveman.travel.domain.PageBean;
import cn.driveman.travel.entity.Favorite;
import cn.driveman.travel.entity.Route;
import cn.driveman.travel.service.IFavoriteService;
import cn.driveman.travel.service.IRouteService;
import cn.driveman.travel.service.impl.FavoriteServiceImpl;
import cn.driveman.travel.service.impl.RouteServiceImpl;
import cn.driveman.travel.vo.ResultInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    IRouteService routeService = new RouteServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");

        Integer cid = 1;
        if(!StringUtils.isEmpty(cidStr)){
            cid =  Integer.parseInt(cidStr);
        }
        String currentPageStr = request.getParameter("currentPage");
        Integer currentPage = 1;
        if(!StringUtils.isEmpty(currentPageStr)){
            currentPage =  Integer.parseInt(currentPageStr);
        }

        if(!StringUtils.isEmpty(rname)){
            rname="%"+rname+"%";
        }

        PageBean<Route> pageBean = routeService.queryPage(cid, currentPage, 10,rname);
        response.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(response.getOutputStream(),pageBean);
    }
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route route = new Route();
        if(!StringUtils.isEmpty(rid)){
             route = routeService.findRouteById(Integer.parseInt(rid));
             route.setCount(routeService.findCountByRid(Integer.parseInt(rid)));
        }

        response.setContentType("application/json;charset=utf-8");

        objectMapper.writeValue(response.getOutputStream(),route);
    }
    //判断用户是否收藏
    public void isFavorite(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException {
        IFavoriteService favoriteService = new FavoriteServiceImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        String uid = request.getParameter("uid");
        String rid = request.getParameter("rid");
        Favorite favorite = null;
        if(!StringUtils.isEmpty(uid)&&!StringUtils.isEmpty(rid)){
            favorite = favoriteService.findFavoriteByRidAndUid(Integer.parseInt(uid), Integer.parseInt(rid));
        }
        response.setContentType("application/json;charset:utf-8");
        objectMapper.writeValue(response.getOutputStream(),favorite);
    }
    //判断用户是否收藏
    public void addFavorite(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException {
        IFavoriteService favoriteService = new FavoriteServiceImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        String uid = request.getParameter("uid");
        String rid = request.getParameter("rid");
        Favorite favorite = null;
        ResultInfo resultInfo = new ResultInfo();
        Integer addInt = 0;
        if(!StringUtils.isEmpty(uid)&&!StringUtils.isEmpty(rid)){
             addInt = favoriteService.addFavoriteByRidAndUid(Integer.parseInt(uid), Integer.parseInt(rid));
        }
        if (addInt > 0) {
            resultInfo.setFlag(true);
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("收藏失败");
        }
        response.setContentType("application/json;charset:utf-8");
        objectMapper.writeValue(response.getOutputStream(),resultInfo);
    }
}
